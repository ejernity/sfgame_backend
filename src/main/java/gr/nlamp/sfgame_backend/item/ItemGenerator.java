package gr.nlamp.sfgame_backend.item;

import gr.nlamp.sfgame_backend.item.strategy.ItemGenerationStrategy;
import gr.nlamp.sfgame_backend.item.strategy.ItemStrategyFactory;
import gr.nlamp.sfgame_backend.player.Class;
import gr.nlamp.sfgame_backend.player.Player;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ItemGenerator {

    private final ItemRepository itemRepository;

    @Transactional
    public void generateWeaponShopItems(final Player player, final boolean isRegistration) {
        if (!isRegistration)
            clearShopItems(player, SlotType.weaponShopSlots);

        for (final SlotType slotType : SlotType.weaponShopSlots) {
            final ItemType itemType = pickRandomWeaponType();
            final Item newItem = generateItem(player, slotType, itemType);
            if (newItem != null) {
                player.getItems().add(newItem);
                newItem.setPlayer(player);
            }
        }
    }

    @Transactional
    public void generateMagicShopItems(final Player player, final boolean isRegistration) {
        if (!isRegistration)
            clearShopItems(player, SlotType.magicShopSlots);

        for (final SlotType slotType : SlotType.magicShopSlots) {
            final ItemType itemType = pickRandomMagicType();
            final Item newItem = generateItem(player, slotType, itemType);
            if (newItem != null) {
                player.getItems().add(newItem);
                newItem.setPlayer(player);
            }
        }
    }

    private void clearShopItems(final Player player, final List<SlotType> slotTypes) {
        itemRepository.deleteItemsByPlayerAndSlotTypes(player.getId(), slotTypes);
    }

    private ItemType pickRandomItemTypeForSlotType(final SlotType slotType) {
        if (slotType.name().contains("WEAPON")) {
            return pickRandomWeaponType();
        } else if (slotType.name().contains("MAGIC")) {
            return pickRandomMagicType();
        } else {
            return null;
        }
    }

    private ItemType pickRandomWeaponType() {
        return ItemType.weaponItemTypes.get(new Random().nextInt(ItemType.weaponItemTypes.size()));
    }

    private ItemType pickRandomMagicType() {
        return ItemType.magicItemTypes.get(new Random().nextInt(ItemType.magicItemTypes.size()));
    }

    public final Item generateItem(final Player player, final SlotType slotType) {
        final ItemType itemType = pickRandomItemTypeForSlotType(slotType);
        return generateItem(player, slotType, itemType);
    }

    public final Item generateItem(final Player player, final SlotType slotType, final ItemType itemType) {
        if (!validateSlotTypeAndItemType(slotType, itemType)) return null;

        final ItemGenerationStrategy strategy = ItemStrategyFactory.getStrategy(itemType);
        return strategy.generateItem(player, slotType, itemType);
    }

    private boolean validateSlotTypeAndItemType(final SlotType slotType, final ItemType itemType) {
        // TODO If truly needed, then implement...
        return true;
    }

}