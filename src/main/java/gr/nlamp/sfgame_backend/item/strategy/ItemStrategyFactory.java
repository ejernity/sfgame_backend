package gr.nlamp.sfgame_backend.item.strategy;

import gr.nlamp.sfgame_backend.item.ItemType;

import java.util.HashMap;
import java.util.Map;

public class ItemStrategyFactory {

    private static final Map<ItemType, ItemGenerationStrategy> strategyMap = new HashMap<>();

    static {
        strategyMap.put(ItemType.POTION, new PotionItemStrategy());
        strategyMap.put(ItemType.DUNGEON_KEY, new DungeonKeyItemStrategy());
        strategyMap.put(ItemType.ALBUM, new AlbumItemStrategy());

        // Add more mappings as needed...
    }

    public static ItemGenerationStrategy getStrategy(ItemType itemType) {
        return strategyMap.getOrDefault(itemType, new DefaultItemStrategy());
    }
}
