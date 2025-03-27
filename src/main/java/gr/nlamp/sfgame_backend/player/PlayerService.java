package gr.nlamp.sfgame_backend.player;

import gr.nlamp.sfgame_backend.initialization.BigDataLoader;
import gr.nlamp.sfgame_backend.item.*;
import gr.nlamp.sfgame_backend.item.booster.Booster;
import gr.nlamp.sfgame_backend.item.booster.BoosterMapper;
import gr.nlamp.sfgame_backend.item.booster.BoosterRepository;
import gr.nlamp.sfgame_backend.player.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ItemRepository itemRepository;
    private final BoosterRepository boosterRepository;

    private final ItemGenerator itemGenerator;

    private final ItemMapper itemMapper = ItemMapper.INSTANCE;
    private final BoosterMapper boosterMapper = BoosterMapper.INSTANCE;
    private final PlayerMapper playerMapper = PlayerMapper.INSTANCE;

    private static BigInteger[] expValues;

    private static BigInteger[] skillValues;

    private final static int MAX_NUM_OF_CHARACTERS_FOR_DESCRIPTION = 255;
    private final static int MUSHROOM_TO_REFRESH_ITEMS = 1;
    private final static int PLAYERS_RANKING_PAGE_SIZE = 25;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeBigNumbers() {
        expValues = BigDataLoader.loadBigNumbersFromXML("src/main/resources/exp_per_level.xml");
        System.out.println("Loaded " + expValues.length + " experience values at startup.");
        skillValues = BigDataLoader.loadBigNumbersFromXML("src/main/resources/coins_per_skill.xml");
        System.out.println("Loaded " + skillValues.length + " skill values at startup.");
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public void increaseSkill(final long playerId, final SkillType skillType) {
        final Player player = getPlayer(playerId);
        increaseSkill(player, skillType);
    }

    public BasicInfoDto getBasicInfo(final long playerId) {
        final Player player = getPlayer(playerId);
        return new BasicInfoDto(player.getLevel(), player.getCoins(), player.getMushrooms());
    }

    public ProfileMainInfoDto getProfileMainInfo(final long playerId) {
        final Player player = getPlayer(playerId);
        final BigInteger expForNextLevel = getExperienceForNextLevel(player.getLevel());
        final ProfileMainInfoDto profileMainInfoDto = new ProfileMainInfoDto();
        profileMainInfoDto.setUsername(player.getUsername());
        profileMainInfoDto.setLevel(player.getLevel());
        profileMainInfoDto.setDescription(player.getDescription());
        profileMainInfoDto.setHonor(player.getHonor());
        profileMainInfoDto.setGainedExperience(player.getGainedExperience());
        profileMainInfoDto.setNumberOfSuccessQuests(player.getNumberOfSuccessQuests());
        profileMainInfoDto.setExperienceForNextLevel(expForNextLevel);
        profileMainInfoDto.setPlayerClass(player.getPlayerClass());
        return profileMainInfoDto;
    }

    public void addExperience(final Player player, final BigInteger experience) {
        player.setGainedExperience(player.getGainedExperience().add(experience));

        BigInteger expForNextLevel = getExperienceForNextLevel(player.getLevel());
        while (player.getGainedExperience().compareTo(expForNextLevel) >= 0) {
            player.setLevel(player.getLevel() + 1);
            expForNextLevel = getExperienceForNextLevel(player.getLevel());
        }
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public void updateDescription(final UpdateDescriptionDto dto, final long playerId) {
        final Player player = getPlayer(playerId);
        String newDescription = dto.getDescription();
        if (dto.getDescription() != null) {
            final int maxLength = Math.min(dto.getDescription().length(), MAX_NUM_OF_CHARACTERS_FOR_DESCRIPTION);
            newDescription = newDescription.substring(0, maxLength);
        }
        player.setDescription(newDescription);
    }

    public EquipmentItemDtoList getEquipment(final long playerId) {
        final List<Item> itemList = itemRepository.findItemsInSlotTypes(playerId, SlotType.equipmentSlots);
        final List<Item> filteredItemListWithoutBoosters = itemList.stream().filter(i -> !i.getItemType().equals(ItemType.POTION)).toList();
        return new EquipmentItemDtoList(itemMapper.mapItemsToEquipmentItemDtos(filteredItemListWithoutBoosters));
    }

    public BagItemDtoList getBag(final long playerId) {
        final List<Item> itemList = itemRepository.findItemsInSlotTypes(playerId, SlotType.bagSlots);
        return new BagItemDtoList(itemMapper.mapItemsToBagItemDtos(itemList));
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public ShopItemDtoList getWeaponShop(final long playerId) {
        final Player player = getPlayer(playerId);
        refreshWeaponShopItemsIfNeeded(player);
        player.setLastWeaponShopAccessDate(System.currentTimeMillis());
        final List<Item> itemList = itemRepository.findItemsInSlotTypes(playerId, SlotType.weaponShopSlots);
        return new ShopItemDtoList(itemMapper.mapItemsToShopItemDtos(itemList));
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public ShopItemDtoList getMagicShop(final long playerId) {
        final Player player = getPlayer(playerId);
        refreshMagicShopItemsIfNeeded(player);
        player.setLastMagicShopAccessDate(System.currentTimeMillis());
        final List<Item> itemList = itemRepository.findItemsInSlotTypes(playerId, SlotType.magicShopSlots);
        return new ShopItemDtoList(itemMapper.mapItemsToShopItemDtos(itemList));
    }

    public BoosterDtoList getBoosters(final long playerId) {
        final List<Booster> boosterList = boosterRepository.findByPlayerIdAndSlotType(playerId, SlotType.EQUIPMENT);
        return new BoosterDtoList(boosterMapper.mapBoostersToBoosterDtos(boosterList));
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public ShopItemDtoList refreshWeaponShop(final long playerId) {
        final Player player = getPlayer(playerId);
        validateHasEnoughMushroomsForRefreshingItems(player.getMushrooms());
        player.setMushrooms(player.getMushrooms() - MUSHROOM_TO_REFRESH_ITEMS);
        itemGenerator.generateWeaponShopItems(player, false);
        // TODO Think what happens if player refresh items
        //  then keep the weapon shop screen open
        //  the day changes
        //  and hits again the screen, so the last access date is yesterday and the items will change!
        final List<Item> itemList = itemRepository.findItemsInSlotTypes(playerId, SlotType.weaponShopSlots);
        return new ShopItemDtoList(itemMapper.mapItemsToShopItemDtos(itemList));
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public ShopItemDtoList refreshMagicShop(final long playerId) {
        final Player player = getPlayer(playerId);
        validateHasEnoughMushroomsForRefreshingItems(player.getMushrooms());
        player.setMushrooms(player.getMushrooms() - MUSHROOM_TO_REFRESH_ITEMS);
        itemGenerator.generateMagicShopItems(player, false);
        // TODO Think what happens if player refresh items
        //  then keep the magic shop screen open
        //  the day changes
        //  and hits again the screen, so the last access date is yesterday and the items will change!
        final List<Item> itemList = itemRepository.findItemsInSlotTypes(playerId, SlotType.magicShopSlots);
        return new ShopItemDtoList(itemMapper.mapItemsToShopItemDtos(itemList));
    }

    public SkillsAnalyticalDto getSkillsAnalytical(final long playerId) {
        final Player player = getPlayer(playerId);
        final SkillsAnalyticalDto result = new SkillsAnalyticalDto();
        result.setBaseStrength(player.getStrength());
        result.setBaseDexterity(player.getDexterity());
        result.setBaseIntelligence(player.getIntelligence());
        result.setBaseConstitution(player.getConstitution());
        result.setBaseLuck(player.getLuck());

        final List<Item> itemList = itemRepository.findItemsInSlotTypes(playerId, SlotType.equipmentSlots);
        final List<Item> provideArmorItems = itemList.stream().filter(i -> ItemType.provideArmorItemTypes.contains(i.getItemType())).toList();
        result.setTotalArmor(provideArmorItems.stream().map(Item::getArmor).reduce(Integer::sum).orElse(0));

        final List<Item> provideSkillsItems = itemList.stream().filter(i -> !i.getItemType().equals(ItemType.POTION)).toList();
        for (final Item item : provideSkillsItems) {
            result.setEquipmentStrength(result.getEquipmentStrength().add(BigInteger.valueOf(item.getStrength())));
            result.setEquipmentDexterity(result.getEquipmentDexterity().add(BigInteger.valueOf(item.getDexterity())));
            result.setEquipmentIntelligence(result.getEquipmentIntelligence().add(BigInteger.valueOf(item.getIntelligence())));
            result.setEquipmentConstitution(result.getEquipmentConstitution().add(BigInteger.valueOf(item.getConstitution())));
            result.setEquipmentLuck(result.getEquipmentLuck().add(BigInteger.valueOf(item.getLuck())));
        }

        final List<Item> boosters = itemList.stream().filter(i -> i.getItemType().equals(ItemType.POTION)).toList();
        for (final Item item : boosters) {
            if (item instanceof Booster booster) {
                final PotionType potionType = booster.getPotionType();

                if (PotionType.STRENGTH_POTIONS.contains(potionType)) {
                    final BigInteger boosterExtraSkill = BigDecimal.valueOf(potionType.getPercentage())
                            .multiply(new BigDecimal(result.getBaseStrength()))
                            .setScale(0, RoundingMode.HALF_UP)
                            .toBigInteger();
                    result.setBoosterStrength(boosterExtraSkill);
                    continue;
                }

                if (PotionType.DEXTERITY_POTIONS.contains(potionType)) {
                    final BigInteger boosterExtraSkill = BigDecimal.valueOf(potionType.getPercentage())
                            .multiply(new BigDecimal(result.getBaseDexterity()))
                            .setScale(0, RoundingMode.HALF_UP)
                            .toBigInteger();
                    result.setBoosterDexterity(boosterExtraSkill);
                    continue;
                }

                if (PotionType.INTELLIGENCE_POTIONS.contains(potionType)) {
                    final BigInteger boosterExtraSkill = BigDecimal.valueOf(potionType.getPercentage())
                            .multiply(new BigDecimal(result.getBaseIntelligence()))
                            .setScale(0, RoundingMode.HALF_UP)
                            .toBigInteger();
                    result.setBoosterIntelligence(boosterExtraSkill);
                    continue;
                }

                if (PotionType.CONSTITUTION_POTIONS.contains(potionType)) {
                    final BigInteger boosterExtraSkill = BigDecimal.valueOf(potionType.getPercentage())
                            .multiply(new BigDecimal(result.getBaseConstitution()))
                            .setScale(0, RoundingMode.HALF_UP)
                            .toBigInteger();
                    result.setBoosterConstitution(boosterExtraSkill);
                    continue;
                }

                if (PotionType.LUCK_POTIONS.contains(potionType)) {
                    final BigInteger boosterExtraSkill = BigDecimal.valueOf(potionType.getPercentage())
                            .multiply(new BigDecimal(result.getBaseLuck()))
                            .setScale(0, RoundingMode.HALF_UP)
                            .toBigInteger();
                    result.setBoosterLuck(boosterExtraSkill);
                }
            }
        }

        // TODO Do some calculation/formula way to produce hit points for the return DTO
        return result;
    }

    public MountDto getMount(long playerId) {
        final Player player = getPlayer(playerId);
        final MountDto mountDto = new MountDto();
        if (player.getMount() == null)
            return mountDto;
        mountDto.setMount(player.getMount());
        mountDto.setActiveUntil(Instant.ofEpochMilli(player.getMountActiveUntil()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        return mountDto;
    }

    public PlayersRankingDto getPlayersRanking(final int page) {
        final Pageable pageable = PageRequest.of(page, PLAYERS_RANKING_PAGE_SIZE, Sort.by("currentRank").ascending());
        final Slice<Player> playerSlice = playerRepository.findPlayers(pageable);
        final Slice<PlayerRankingDto> playerRankingDtoSlice =  new SliceImpl<>(playerMapper.mapPlayerListToPlayerRankingDtoList(playerSlice), playerSlice.getPageable(), playerSlice.hasNext());
        return new PlayersRankingDto(playerRankingDtoSlice);
    }

    private void validateHasEnoughMushroomsForRefreshingItems(final long mushrooms) {
        if (mushrooms < MUSHROOM_TO_REFRESH_ITEMS)
            throw new RuntimeException("Not enough mushrooms to refresh items.");
    }

    private void refreshWeaponShopItemsIfNeeded(Player player) {
        if (player.getLastWeaponShopAccessDate() == null)
            return;
        final LocalDate now = LocalDate.now();
        final LocalDate lastWeaponShopAccessDate = Instant.ofEpochMilli(player.getLastWeaponShopAccessDate()).atZone(ZoneId.systemDefault()).toLocalDate();
        if (!now.equals(lastWeaponShopAccessDate)) {
            itemGenerator.generateWeaponShopItems(player, false);
        }
    }

    private void refreshMagicShopItemsIfNeeded(Player player) {
        if (player.getLastMagicShopAccessDate() == null)
            return;
        final LocalDate now = LocalDate.now();
        final LocalDate lastMagicShopAccessDate = Instant.ofEpochMilli(player.getLastMagicShopAccessDate()).atZone(ZoneId.systemDefault()).toLocalDate();
        if (!now.equals(lastMagicShopAccessDate)) {
            itemGenerator.generateMagicShopItems(player, false);
        }
    }

    private void increaseSkill(final Player player, final SkillType skillType) {
        BigInteger skillCoinCost;
        switch (skillType) {
            case STRENGTH -> {
                skillCoinCost = getCostForNextSkillValue(player.getStrength());
                player.setStrength(player.getStrength().add(BigInteger.ONE));
            }
            case DEXTERITY -> {
                skillCoinCost = getCostForNextSkillValue(player.getDexterity());
                player.setDexterity(player.getDexterity().add(BigInteger.ONE));
            }
            case INTELLIGENCE -> {
                skillCoinCost = getCostForNextSkillValue(player.getIntelligence());
                player.setIntelligence(player.getIntelligence().add(BigInteger.ONE));
            }
            case CONSTITUTION -> {
                skillCoinCost = getCostForNextSkillValue(player.getConstitution());
                player.setConstitution(player.getConstitution().add(BigInteger.ONE));
            }
            case LUCK -> {
                skillCoinCost = getCostForNextSkillValue(player.getLuck());
                player.setLuck(player.getLuck().add(BigInteger.ONE));
            }
            default -> {
                // TODO Check if it is enough...
                throw new RuntimeException("Unknown skill type: " + skillType);
            }
        }
        if (player.getCoins().compareTo(skillCoinCost) < 0)
            throw new RuntimeException("Not enough coins to increase skill.");
        player.setCoins(player.getCoins().subtract(skillCoinCost));
    }

    private BigInteger getCostForNextSkillValue(final BigInteger skillCurrentValue) {
        if (skillCurrentValue.compareTo(BigInteger.valueOf(3151)) > 0)
            return BigInteger.valueOf(10000000);
        // TODO Issue for new players with starting value for a skill at negative number, OOB Exception for loaded coins/per/skill array
        return skillValues[Math.max(skillCurrentValue.intValue(), 0)];
    }

    private static BigInteger getExperienceForNextLevel(final long level) {
        BigInteger experience;

        if(level > 768){
            experience = expValues[768];

            final BigDecimal multiplier = new BigDecimal("1.4");
            for(int i = 768; i < level; i++) {
                BigDecimal experienceDecimal = new BigDecimal(experience);

                experienceDecimal = experienceDecimal.multiply(multiplier);

                experience = experienceDecimal.toBigInteger();
            }

            final BigInteger max = new BigDecimal("4.3026616884253E+303").toBigInteger();
            if(experience.compareTo(max) > 0)
                experience = new BigDecimal("4.3026616884253E+303").toBigInteger();

            return experience;
        }
        else
            return expValues[(int) level];
    }

    private Player getPlayer(final long playerId) {
        final Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null)
            throw new RuntimeException("Player not found");
        return player;
    }
}
