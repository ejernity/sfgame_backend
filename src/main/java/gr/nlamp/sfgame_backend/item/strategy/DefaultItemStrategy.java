package gr.nlamp.sfgame_backend.item.strategy;

import gr.nlamp.sfgame_backend.item.Item;
import gr.nlamp.sfgame_backend.item.ItemRarity;
import gr.nlamp.sfgame_backend.item.ItemType;
import gr.nlamp.sfgame_backend.item.SlotType;
import gr.nlamp.sfgame_backend.player.Class;
import gr.nlamp.sfgame_backend.player.Player;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultItemStrategy implements ItemGenerationStrategy {

    @Override
    public Item generateItem(Player player, SlotType slotType, ItemType itemType) {
        Item item = new Item();
        item.setItemType(itemType);
        item.setSlotType(slotType);
        item.setItemId(123); // TODO Placeholder for actual item ID logic

        // Assign rarity
        setRarity(item);

        // Assign stats
        setSkillValues(item, player);
        setArmorValue(item, player);
        setCosts(item, player);

        item.setPlayer(player);
        player.getItems().add(item);
        return item;
    }

    private void setRarity(final Item item) {
        final double rarityRoll = Math.random();
        if (rarityRoll < 0.01) { // 1% chance for Legendary
            item.setItemRarity(ItemRarity.LEGENDARY);
        } else if (rarityRoll < 0.1) { // 10% chance for Epic
            item.setItemRarity(ItemRarity.EPIC);
        } else {
            item.setItemRarity(ItemRarity.COMMON);
        }
    }

    private void setSkillValues(final Item item, final Player player) {
        final long baseValue = player.getLevel() * 5; // Base scaling with player level
        final double multiplier = switch (item.getItemRarity()) {
            case LEGENDARY -> 3.0;
            case EPIC -> 2.0;
            case COMMON -> 1.0;
        };

        final int variance = (int) (Math.random() * baseValue * 0.2); // ±20% variance

        switch (item.getItemRarity()) {
            case COMMON -> assignOneOrTwoSkills(item, baseValue, multiplier, variance);
            case EPIC -> assignEpicSkills(item, baseValue, multiplier, variance, player.getPlayerClass());
            case LEGENDARY -> assignLegendarySkills(item, baseValue, multiplier, variance);
        }
    }

    private void assignOneOrTwoSkills(final Item item, final long baseValue, final double multiplier, final int variance) {
        final int totalSkillValue = (int) (baseValue * multiplier + variance);

        // Determine whether to assign points to one or two skills
        final int numberOfSkills = (Math.random() < 0.5) ? 1 : 2; // 50% chance for one skill, 50% chance for two

        if (numberOfSkills == 1) {
            // Assign all points to a single skill
            final int skillIndex = generateRandomSkills(1).get(0); // Pick one random skill
            assignSkillByIndex(item, skillIndex, totalSkillValue);
        } else {
            // Split points between two skills
            final List<Integer> skillIndices = generateRandomSkills(2); // Pick two random skills
            final int firstSkillValue = (int) (totalSkillValue * Math.random()); // Random split for the first skill
            final int secondSkillValue = totalSkillValue - firstSkillValue; // Remaining points for the second skill

            assignSkillByIndex(item, skillIndices.get(0), firstSkillValue);
            assignSkillByIndex(item, skillIndices.get(1), secondSkillValue);
        }
    }

    private void assignSkillByIndex(final Item item, final int skillIndex, final int value) {
        switch (skillIndex) {
            case 0 -> item.setStrength(value);
            case 1 -> item.setDexterity(value);
            case 2 -> item.setIntelligence(value);
            case 3 -> item.setConstitution(value);
            case 4 -> item.setLuck(value);
        }
    }

    private List<Integer> generateRandomSkills(final int numberOfSkills) {
        final List<Integer> skillIndices = new ArrayList<>(List.of(0, 1, 2, 3, 4));
        Collections.shuffle(skillIndices);
        return skillIndices.subList(0, numberOfSkills); // Return the first 'n' indices
    }


    private void assignEpicSkills(final Item item, final long baseValue,
                                  final double multiplier, final int variance,
                                  final Class playerClass) {
        final int skillValue = (int) (baseValue * multiplier + variance);

        // Determine class-specific base skills
        switch (playerClass) {
            case WARRIOR -> {
                item.setStrength(skillValue);
                item.setConstitution(skillValue);
                item.setLuck(skillValue);
            }
            case MAGE -> {
                item.setIntelligence(skillValue);
                item.setConstitution(skillValue);
                item.setLuck(skillValue);
            }
            case SCOUT -> {
                item.setDexterity(skillValue);
                item.setConstitution(skillValue);
                item.setLuck(skillValue);
            }
            default -> assignAllSkills(item, baseValue, multiplier, variance); // Fallback to all skills
        }
    }

    private void assignLegendarySkills(final Item item, final long baseValue, final double multiplier, final int variance) {
        // Assign to all skills
        assignAllSkills(item, baseValue, multiplier, variance);
    }

    private void assignAllSkills(final Item item, final long baseValue, final double multiplier, final int variance) {
        item.setStrength((int) (baseValue * multiplier + variance));
        item.setDexterity((int) (baseValue * multiplier + variance));
        item.setIntelligence((int) (baseValue * multiplier + variance));
        item.setConstitution((int) (baseValue * multiplier + variance));
        item.setLuck((int) (baseValue * multiplier + variance));
    }

    private void setArmorValue(final Item item, final Player player) {
        if (ItemType.provideArmorItemTypes.contains(item.getItemType())) {
            final long baseArmor = player.getLevel() * 10;
            final double multiplier = switch (item.getItemRarity()) {
                case LEGENDARY -> 3.0;
                case EPIC -> 2.0;
                case COMMON -> 1.0;
            };
            item.setArmor((int) (baseArmor * multiplier));
        }
    }

    private void setCosts(final Item item, final Player player) {
        final long baseCoinCost = player.getLevel() * 20;  // Base cost starts at 20 coins at level 1
        final double multiplier = switch (item.getItemRarity()) {
            case LEGENDARY -> 2.5;
            case EPIC -> 1.5;
            case COMMON -> 1.0;
        };

        // Calculate the coin cost, adding randomness (±20%)
        long calculatedCoinCost = (long) (baseCoinCost * multiplier);

        // Introduce a random variance factor (between 0.8 and 1.2)
        final double randomVariance = 0.8 + (Math.random() * 0.4);  // Random factor between 0.8 and 1.2
        calculatedCoinCost = (long) (calculatedCoinCost * randomVariance);

        // Set the final coin cost for the item, ensuring it doesn't go below 1 coin
        item.setCoinCost(BigInteger.valueOf(Math.max(calculatedCoinCost, 1)));  // Ensure minimum 1 coin

        // Mushroom cost logic with a higher probability for Epic and Legendary items
        if (Math.random() < getMushroomChance(item)) {
            long baseMushCost = 1;  // Default base mushroom cost
            if (item.getItemRarity() == ItemRarity.EPIC) {
                // Epic items can cost 1 or 2 mushrooms (75% chance for 1, 25% for 2)
                baseMushCost = (Math.random() < 0.75) ? 1 : 2;
            } else if (item.getItemRarity() == ItemRarity.LEGENDARY) {
                // Legendary items can cost 2 or 3 mushrooms (50% chance for 2, 50% for 3)
                baseMushCost = (Math.random() < 0.5) ? 2 : 3;
            }

            item.setMushCost(baseMushCost);  // Set mushroom cost
        } else {
            item.setMushCost(0L);  // No mushroom cost
        }
    }

    private double getMushroomChance(final Item item) {
        return switch (item.getItemRarity()) {
            case LEGENDARY -> 0.30;  // 30% chance for Legendary
            case EPIC -> 0.20;       // 20% chance for Epic
            case COMMON -> 0.10;     // 10% chance for Common
        };
    }
}
