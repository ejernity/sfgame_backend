package gr.nlamp.sfgame_backend.item.strategy;

import gr.nlamp.sfgame_backend.item.*;
import gr.nlamp.sfgame_backend.player.Player;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class DungeonKeyItemStrategy implements ItemGenerationStrategy {

    @Override
    public Item generateItem(Player player, SlotType slotType, ItemType itemType) {
        final Item item = new Item();
        item.setItemType(itemType);
        item.setSlotType(slotType);
        item.setItemId(999); // TODO Placeholder ID for potions
        item.setCoinCost(BigInteger.ZERO);
        item.setMushCost(0L);

        item.setPlayer(player);
        player.getItems().add(item);
        return item;
    }
}
