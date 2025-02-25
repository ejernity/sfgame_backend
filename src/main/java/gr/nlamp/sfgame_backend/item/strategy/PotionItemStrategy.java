package gr.nlamp.sfgame_backend.item.strategy;

import gr.nlamp.sfgame_backend.item.Item;
import gr.nlamp.sfgame_backend.item.ItemType;
import gr.nlamp.sfgame_backend.item.PotionType;
import gr.nlamp.sfgame_backend.item.SlotType;
import gr.nlamp.sfgame_backend.item.booster.Booster;
import gr.nlamp.sfgame_backend.player.Player;

import java.math.BigInteger;
import java.util.Random;

public class PotionItemStrategy implements ItemGenerationStrategy {

    private static final PotionType[] potionTypes = PotionType.values();
    private static final Random random = new Random();

    @Override
    public Item generateItem(Player player, SlotType slotType, ItemType itemType) {
        final Booster potion = new Booster();
        potion.setItemType(itemType);
        potion.setSlotType(slotType);
        potion.setPotionType(potionTypes[random.nextInt(potionTypes.length)]);
        potion.setItemId(999); // TODO Placeholder ID for potions
        potion.setCoinCost(getCoinCost(potion, player)); // Fixed or scaled cost
        potion.setMushCost(0L);
        if (potion.getPotionType().equals(PotionType.ETERNAL_LIFE))
            potion.setMushCost(25L);

        potion.setPlayer(player);
        player.getItems().add(potion);
        return potion;
    }

    private BigInteger getCoinCost(Booster booster, Player player) {
        final long baseCoinCost = player.getLevel() * 20;  // Base cost starts at 20 coins at level 1
        final double multiplier = switch (booster.getPotionType().name().split("_")[1]) {
            case "SMALL" -> 1.0;
            case "MEDIUM" -> 1.5;
            case "LARGE" -> 2.5;
            case "LIFE" -> 2.0;
            default ->
                    throw new IllegalStateException("Unexpected value: " + booster.getPotionType().name().split("_")[1]);
        };

        // Calculate the coin cost, adding randomness (±20%)
        long calculatedCoinCost = (long) (baseCoinCost * multiplier);

        // Introduce a random variance factor (between 0.8 and 1.2)
        final double randomVariance = 0.8 + (Math.random() * 0.4);  // Random factor between 0.8 and 1.2
        calculatedCoinCost = (long) (calculatedCoinCost * randomVariance);

        // Set the final coin cost for the item, ensuring it doesn't go below 1 coin
        return BigInteger.valueOf(Math.max(calculatedCoinCost, 1));  // Ensure minimum 1 coin
    }
}
