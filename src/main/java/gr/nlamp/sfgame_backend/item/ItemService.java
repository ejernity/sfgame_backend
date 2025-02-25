package gr.nlamp.sfgame_backend.item;

import gr.nlamp.sfgame_backend.item.booster.Booster;
import gr.nlamp.sfgame_backend.item.booster.BoosterRepository;
import gr.nlamp.sfgame_backend.item.dto.MoveItemRequestDto;
import gr.nlamp.sfgame_backend.player.Player;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final BoosterRepository boosterRepository;

    private final ItemGenerator itemGenerator;

    private static final String SLOT_GROUP_BAG = "BAG";
    private static final String SLOT_GROUP_SHOP = "SHOP";
    private static final BigInteger SELLING_PERCENTAGE = BigInteger.valueOf(4);
    private static final int MAX_ACTIVE_BOOSTERS = 3;

    private static final List<SlotType> bagSlots = List.of(SlotType.BAG_1, SlotType.BAG_2, SlotType.BAG_3,
            SlotType.BAG_4, SlotType.BAG_5, SlotType.BAG_6, SlotType.BAG_7, SlotType.BAG_8, SlotType.BAG_9,
            SlotType.BAG_10, SlotType.BAG_11, SlotType.BAG_12, SlotType.BAG_13, SlotType.BAG_14, SlotType.BAG_15,
            SlotType.BAG_16);

    // TODO: Future feature allow movement between equipped items and items in bag
    public void moveBagItem(final long playerId, final MoveItemRequestDto dto) {
        final Item item = getItemIfExists(dto.getItemId());
        if (item.getSlotType().equals(dto.getDestinationSlot()))
            return;
        validateItemBelongsToPlayer(item, playerId);
        validateSlotTypeBelongsToGroupOfItems(item.getSlotType(), SLOT_GROUP_BAG);

        validateSlotTypeBelongsToGroupOfItems(dto.getDestinationSlot(), SLOT_GROUP_BAG);
        final Item itemFromDestinationSlot = getItemFromDestinationSlot(playerId, dto.getDestinationSlot());
        final boolean isSwap = itemFromDestinationSlot != null;
        if (isSwap) {
            itemFromDestinationSlot.setSlotType(item.getSlotType());
            itemRepository.save(itemFromDestinationSlot);
        }

        item.setSlotType(dto.getDestinationSlot());
        itemRepository.save(item);
    }

    public void equip(final long playerId, final long itemId) {
        final Item item = getItemIfExists(itemId);
        validateItemBelongsToPlayer(item, playerId);
        validateSlotTypeBelongsToGroupOfItems(item.getSlotType(), SLOT_GROUP_BAG);

        final Item alreadyEquippedItem = getItemFromDestinationSlot(playerId, SlotType.EQUIPMENT, item.getItemType());
        final boolean isSwap = alreadyEquippedItem != null;
        if (isSwap) {
            alreadyEquippedItem.setSlotType(item.getSlotType());
            itemRepository.save(alreadyEquippedItem);
        }

        item.setSlotType(SlotType.EQUIPMENT);
        itemRepository.save(item);
    }

    public void unequip(final long playerId, final long itemId) {
        final Item item = getItemIfExists(itemId);
        validateItemBelongsToPlayer(item, playerId);
        validateSlotTypeBelongsToGroupOfItems(item.getSlotType(), SlotType.EQUIPMENT.name());

        final List<Item> itemsFromBag = getItemsFromBag(playerId);
        final SlotType firstAvailableSlotInBag = findFirstAvailableSlotInBag(itemsFromBag);
        if (firstAvailableSlotInBag == null)
            throw new RuntimeException("No slot available to unequip item.");

        item.setSlotType(firstAvailableSlotInBag);
        itemRepository.save(item);
    }

    public void buy(final long playerId, final long itemId) {
        final Item item = getItemIfExists(itemId);
        validateItemBelongsToPlayer(item, playerId);
        validateSlotTypeBelongsToGroupOfItems(item.getSlotType(), SLOT_GROUP_SHOP);

        final List<Item> itemsFromBag = getItemsFromBag(playerId);
        final SlotType firstAvailableSlotInBag = findFirstAvailableSlotInBag(itemsFromBag);
        if (firstAvailableSlotInBag == null)
            throw new RuntimeException("No slot available to unequip item.");

        final Player player = item.getPlayer();
        validatePlayerHasEnoughResourcesToBuyItem(player, item);

        player.setCoins(player.getCoins().subtract(item.getCoinCost()));
        player.setMushrooms(player.getMushrooms() - item.getMushCost());

        final SlotType shopSlotType = item.getSlotType();
        final Item newItemForShop = itemGenerator.generateItem(player, shopSlotType);
        itemRepository.save(newItemForShop);

        item.setSlotType(firstAvailableSlotInBag);
        itemRepository.save(item);
    }

    @Transactional(rollbackOn = Exception.class)
    public void sell(final long playerId, final long itemId) {
        final Item item = getItemIfExists(itemId);
        validateItemBelongsToPlayer(item, playerId);
        validateSlotTypeBelongsToGroupOfItems(item.getSlotType(), List.of(SlotType.EQUIPMENT.name(), SLOT_GROUP_BAG));

        // Kanei stroggylopoiisi pros ta katw
        final BigInteger sellingPrice = item.getCoinCost().divide(SELLING_PERCENTAGE); // 25% of the buying price
        final Player player = item.getPlayer();
        player.setCoins(player.getCoins().add(sellingPrice));
        itemRepository.deleteItemById(item.getId());
    }

    public void consumePotion(final long playerId, final long boosterId) {
        final Booster booster = getBoosterIfExists(boosterId);
        validateItemBelongsToPlayer(booster, playerId);
        validateSlotTypeBelongsToGroupOfItems(booster.getSlotType(), SLOT_GROUP_BAG);

        final Player player = booster.getPlayer();
        clearInactiveBoosters(player);

        validateMaxActiveBoostersForPlayer(player);

        final Optional<Booster> optionalExistingBooster = player.getBoosters()
                .stream().filter(b -> b.getPotionType().name().split("_")[0].equals(booster.getPotionType().name().split("_")[0]))
                .findFirst();
        final boolean existingBoosterWithSamePotionTypeCategory = optionalExistingBooster.isPresent();
        if (existingBoosterWithSamePotionTypeCategory) {
            final Booster existingBooster = optionalExistingBooster.get();
            if (existingBooster.getPotionType().getPercentage() == booster.getPotionType().getPercentage()) {
                existingBooster.setActiveUntil(existingBooster.getActiveUntil() + booster.getPotionType().getDays() * 86400000);
                boosterRepository.delete(booster);
                boosterRepository.save(existingBooster);
            } else if (existingBooster.getPotionType().getPercentage() < booster.getPotionType().getPercentage()) {
                boosterRepository.delete(existingBooster);
                booster.setSlotType(SlotType.EQUIPMENT);
                booster.setActiveUntil(System.currentTimeMillis() + booster.getPotionType().getDays() * 86400000);
                boosterRepository.save(booster);
            }
        } else {
            booster.setSlotType(SlotType.EQUIPMENT);
            booster.setActiveUntil(System.currentTimeMillis() + booster.getPotionType().getDays() * 86400000);
            boosterRepository.save(booster);
        }
    }

    private void validateMaxActiveBoostersForPlayer(final Player player) {
        final int activeBoosters = boosterRepository.countActiveBoostersForPlayer(player.getId());
        if (activeBoosters == MAX_ACTIVE_BOOSTERS)
            throw new RuntimeException("You have reached the maximum number of active boosters.");
    }

    private void validatePlayerHasEnoughResourcesToBuyItem(final Player player, final Item item) {
        if (player.getCoins().compareTo(item.getCoinCost()) < 0 || player.getMushrooms().compareTo(item.getMushCost()) < 0)
            throw new RuntimeException("You do not have enough resources to buy it.");
    }

    private SlotType findFirstAvailableSlotInBag(final List<Item> itemsFromBag) {
        final List<SlotType> usedSlots = itemsFromBag.stream()
                .map(Item::getSlotType)
                .toList();
        return bagSlots.stream()
                .filter(slot -> !usedSlots.contains(slot))
                .findFirst()
                .orElse(null);
    }

    private void validateItemBelongsToPlayer(final Item item, final long playerId) {
        if (!item.getPlayer().getId().equals(playerId))
            throw new RuntimeException("Item did not belong to the player.");
    }

    private void validateSlotTypeBelongsToGroupOfItems(final SlotType slotType, final String slotGroup) {
        if (!slotType.name().contains(slotGroup))
            throw new RuntimeException(String.format("Slot type did not belong to the %s.", slotGroup));
    }

    private void validateSlotTypeBelongsToGroupOfItems(final SlotType slotType, final List<String> slotGroupList) {
        final boolean belongsToGroup = slotGroupList.stream()
                .anyMatch(slotGroup -> slotType.name().contains(slotGroup));

        if (!belongsToGroup)
            throw new RuntimeException(String.format("Slot type did not belong to any of the specified groups: %s.", slotGroupList));
    }

    private Item getItemFromDestinationSlot(final long playerId, final SlotType slotType) {
        final Optional<Item> optionalItem = itemRepository.findBySlotTypeAndPlayerId(slotType, playerId);
        return optionalItem.orElse(null);
    }

    private Item getItemFromDestinationSlot(final long playerId, final SlotType slotType, final ItemType itemType) {
        final Optional<Item> optionalItem = itemRepository.findBySlotTypeAndPlayerIdAndItemType(slotType, playerId, itemType);
        return optionalItem.orElse(null);
    }

    private Item getItemIfExists(final long itemId) {
        final Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty())
            throw new RuntimeException("Item did not exist.");

        return optionalItem.get();
    }

    private Booster getBoosterIfExists(final long boosterId) {
        final Optional<Booster> optionalBooster = boosterRepository.findById(boosterId);
        if (optionalBooster.isEmpty())
            throw new RuntimeException("Booster did not exist.");

        return optionalBooster.get();
    }

    private List<Item> getItemsFromBag(final long playerId) {
        return itemRepository.findItemsInSlotTypes(playerId, bagSlots);
    }

    public void clearInactiveBoosters(final Player player) {
        final List<Booster> boosters = player.getBoosters()
                .stream()
                .filter(booster -> booster.getSlotType().equals(SlotType.EQUIPMENT)).toList();
        final List<Long> boosterIdsToDelete = new ArrayList<>();
        for (final Booster booster : boosters) {
            if (booster.getActiveUntil() < System.currentTimeMillis()) {
                booster.setPlayer(null);
                player.getBoosters().remove(booster);
                boosterIdsToDelete.add(booster.getId());
            }
        }
        boosterRepository.deleteAllById(boosterIdsToDelete);
    }
}
