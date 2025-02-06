package gr.nlamp.sfgame_backend.item.strategy;

import gr.nlamp.sfgame_backend.item.Item;
import gr.nlamp.sfgame_backend.item.ItemType;
import gr.nlamp.sfgame_backend.item.SlotType;
import gr.nlamp.sfgame_backend.player.Player;

public interface ItemGenerationStrategy {
    Item generateItem(Player player, SlotType slotType, ItemType itemType);
}
