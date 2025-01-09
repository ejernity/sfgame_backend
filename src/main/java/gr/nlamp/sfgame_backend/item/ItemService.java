package gr.nlamp.sfgame_backend.item;

import gr.nlamp.sfgame_backend.item.dto.MoveItemRequestDto;
import gr.nlamp.sfgame_backend.player.Player;
import gr.nlamp.sfgame_backend.player.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemGenerator itemGenerator;

    private static final String SLOT_GROUP_BAG = "BAG";
    private static final String SLOT_GROUP_SHOP = "SHOP";

    private static final List<SlotType> bagSlots = List.of(SlotType.BAG_1, SlotType.BAG_2, SlotType.BAG_3,
            SlotType.BAG_4, SlotType.BAG_5, SlotType.BAG_6, SlotType.BAG_7, SlotType.BAG_8, SlotType.BAG_9,
            SlotType.BAG_10, SlotType.BAG_11, SlotType.BAG_12, SlotType.BAG_13, SlotType.BAG_14, SlotType.BAG_15,
            SlotType.BAG_16);

    private static final int BAG_SPACE = 16;
    private final PlayerRepository playerRepository;

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

    public void sell(final long playerId, final long itemId) {

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

    private List<Item> getItemsFromBag(final long playerId) {
        return itemRepository.findItemsInSlotTypes(playerId, bagSlots);
    }
}
