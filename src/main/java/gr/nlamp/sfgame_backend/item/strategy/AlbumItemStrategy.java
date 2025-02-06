package gr.nlamp.sfgame_backend.item.strategy;

import gr.nlamp.sfgame_backend.item.Item;
import gr.nlamp.sfgame_backend.item.ItemType;
import gr.nlamp.sfgame_backend.item.SlotType;
import gr.nlamp.sfgame_backend.player.Player;

import java.math.BigInteger;

public class AlbumItemStrategy implements ItemGenerationStrategy {

    @Override
    public Item generateItem(Player player, SlotType slotType, ItemType itemType) {
        final Item item = new Item();
        item.setItemType(itemType);
        item.setSlotType(slotType);
        item.setItemId(999); // TODO Placeholder ID for potions
        item.setCoinCost(BigInteger.valueOf(25));
        item.setMushCost(0L);

        item.setPlayer(player);
        player.getItems().add(item);
        return item;
    }
}
