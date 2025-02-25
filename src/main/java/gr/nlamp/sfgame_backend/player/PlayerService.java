package gr.nlamp.sfgame_backend.player;

import gr.nlamp.sfgame_backend.initialization.BigDataLoader;
import gr.nlamp.sfgame_backend.item.Item;
import gr.nlamp.sfgame_backend.item.ItemMapper;
import gr.nlamp.sfgame_backend.item.ItemRepository;
import gr.nlamp.sfgame_backend.item.SlotType;
import gr.nlamp.sfgame_backend.player.dto.BasicInfoDto;
import gr.nlamp.sfgame_backend.player.dto.EquipmentItemDtoList;
import gr.nlamp.sfgame_backend.player.dto.ProfileMainInfoDto;
import gr.nlamp.sfgame_backend.player.dto.UpdateDescriptionDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper = ItemMapper.INSTANCE;

    private static BigInteger[] expValues;

    private static BigInteger[] skillValues;

    private final static int MAX_NUM_OF_CHARACTERS_FOR_DESCRIPTION = 255;

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
        return new EquipmentItemDtoList(itemMapper.mapItemsToEquipmentItemDtos(itemList));
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
        return skillValues[skillCurrentValue.intValue()];
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
