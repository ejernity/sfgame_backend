package gr.nlamp.sfgame_backend.item;

import gr.nlamp.sfgame_backend.item.dto.MoveItemRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private static final String SLOT_GROUP_BAG = "BAG";

    public void moveBagItem(final long playerId, final MoveItemRequestDto dto) {
        final Optional<Item> optionalItem = itemRepository.findById(dto.getItemId());
        if (optionalItem.isEmpty())
            throw new RuntimeException("Item did not exist.");

        final Item item = optionalItem.get();
        if (item.getSlotType().equals(dto.getDestinationSlot()))
            return;
        validateItemBelongsToPlayer(item, playerId);
        validateSlotTypeBelongsToInventory(item.getSlotType(), SLOT_GROUP_BAG);

        final Item itemFromDestinationSlot = getItemFromDestinationSlot(playerId, dto.getDestinationSlot());
        final boolean isSwap = itemFromDestinationSlot != null;
        if (isSwap) {
            validateSlotTypeBelongsToInventory(itemFromDestinationSlot.getSlotType(), SLOT_GROUP_BAG);
            itemFromDestinationSlot.setSlotType(item.getSlotType());
            itemRepository.save(itemFromDestinationSlot);
        }

        item.setSlotType(dto.getDestinationSlot());
        itemRepository.save(item);
    }

    private void validateItemBelongsToPlayer(final Item item, final long playerId) {
        if (!item.getPlayer().getId().equals(playerId))
            throw new RuntimeException("Item did not belong to the player.");
    }

    private void validateSlotTypeBelongsToInventory(final SlotType slotType, final String slotGroup) {
        if (!slotType.name().contains(slotGroup))
            throw new RuntimeException("Slot type did not belong to the inventory.");
    }

    private Item getItemFromDestinationSlot(final long playerId, final SlotType slotType) {
        final Optional<Item> optionalItem = itemRepository.findBySlotTypeAndPlayerId(slotType, playerId);
        return optionalItem.orElse(null);
    }
}
