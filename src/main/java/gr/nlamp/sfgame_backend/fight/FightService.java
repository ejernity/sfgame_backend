package gr.nlamp.sfgame_backend.fight;

import gr.nlamp.sfgame_backend.item.Item;
import gr.nlamp.sfgame_backend.item.ItemRepository;
import gr.nlamp.sfgame_backend.item.ItemType;
import gr.nlamp.sfgame_backend.item.SlotType;
import gr.nlamp.sfgame_backend.player.Class;
import gr.nlamp.sfgame_backend.player.Player;
import gr.nlamp.sfgame_backend.player.PlayerService;
import gr.nlamp.sfgame_backend.player.dto.SkillsAnalyticalDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class FightService {
    private final PlayerService playerService;
    private final ItemRepository itemRepository;

    // PvP
    public List<AttackResultDto> simulatePvpFight(final long attackerId, final long defenderId) {
        if (attackerId == defenderId) {
            throw new RuntimeException("You cannot fight yourself");
        }
        // Find players
        final Player attacker = playerService.getPlayer(attackerId);
        final Player defender = playerService.getPlayer(defenderId);

        // Find weapon for players
        final Item attackerWeapon = getWeapon(attacker);
        final Item defenderWeapon = getWeapon(defender);

        // Find shield for players
        final Item attackerShield = getShield(attacker);
        final Item defenderShield = getShield(defender);

        // Calculate skills
        final SkillsAnalyticalDto attackerSkills = playerService.getSkillsAnalytical(attacker);
        final SkillsAnalyticalDto defenderSkills = playerService.getSkillsAnalytical(defender);

        final List<AttackResultDto> result = new ArrayList<>();
        boolean playerAlive = true, targetPlayerAlive = true;
        boolean attackerTurn = attacker.getDexterity().compareTo(defender.getDexterity()) >= 0;

        while (playerAlive && targetPlayerAlive) {

            final AttackResultDto turnResult;
            if (attackerTurn) {
                turnResult = performAttack(
                        attacker, defender,
                        attackerWeapon, defenderShield,
                        attackerSkills, defenderSkills
                );
            } else {
                turnResult = performAttack(
                        defender, attacker,
                        defenderWeapon, attackerShield,
                        defenderSkills, attackerSkills
                );
            }
            result.add(turnResult);

            playerAlive = attackerSkills.getHitPoints().compareTo(BigInteger.ZERO) > 0;
            targetPlayerAlive = defenderSkills.getHitPoints().compareTo(BigInteger.ZERO) > 0;

            attackerTurn = !attackerTurn;
        }

        return result;
    }

    private AttackResultDto performAttack(Player attacker, Player defender,
                                          Item attackerWeapon, Item defenderShield,
                                          SkillsAnalyticalDto attackerSkills, SkillsAnalyticalDto defenderSkills) {
        final Random rnd = new Random();

        // 1. Βρες damage source
        int baseDamage = switch (attacker.getPlayerClass()) {
            case WARRIOR -> attackerSkills.getBaseStrength().add(attackerSkills.getEquipmentStrength()).add(attackerSkills.getBoosterStrength()).intValue();
            case SCOUT  -> attackerSkills.getBaseDexterity().add(attackerSkills.getEquipmentDexterity()).add(attackerSkills.getBoosterDexterity()).intValue();
            case MAGE    -> attackerSkills.getBaseIntelligence().add(attackerSkills.getEquipmentIntelligence()).add(attackerSkills.getBoosterIntelligence()).intValue();
        };

        if (attackerWeapon != null) {
            // TODO Create district weapon class that has from its generation final values for min & max
            final int[] weaponRange = calculateWeaponDamageRange(attackerWeapon, attacker.getLevel(), attacker.getPlayerClass());
            final int minDamage = weaponRange[0];
            final int maxDamage = weaponRange[1];
            baseDamage += rnd.nextInt(minDamage, maxDamage + 1);
        }

        // 2. Υπολόγισε critical chance
        boolean critical = rnd.nextDouble() < attacker.getLuck().doubleValue() / 100.0;
        if (critical) {
            baseDamage *= 2; // διπλό damage για crit
        }

        // 3. Dodge / Block
        boolean dodged = false;
        boolean blocked = false;

        if (defender.getPlayerClass() == Class.SCOUT) {
            dodged = rnd.nextDouble() < defender.getDexterity().doubleValue() / 200.0;
        }
        if (defender.getPlayerClass() == Class.WARRIOR && defenderShield != null) {
            blocked = rnd.nextDouble() < defender.getConstitution().doubleValue() / 300.0;
        }

        if (dodged) {
            return new AttackResultDto(attacker.getId(), defender.getId(), 0, true, false, false, defenderSkills.getHitPoints());
        }
        if (blocked) {
            return new AttackResultDto(attacker.getId(), defender.getId(), 0, false, true, false, defenderSkills.getHitPoints());
        }

        // 4. Armor mitigation
        int finalDamage = baseDamage - defenderSkills.getTotalArmor();
        if (finalDamage < 0) finalDamage = 0;

        // 5. Reduce defender hit points
        defenderSkills.setHitPoints(defenderSkills.getHitPoints().subtract(BigInteger.valueOf(finalDamage)));

        return new AttackResultDto(attacker.getId(), defender.getId(), finalDamage, false, false, critical, defenderSkills.getHitPoints());
    }

    private Item getWeapon(Player player) {
        final long playerId = player.getId();
        return itemRepository.findBySlotTypeAndPlayerIdAndItemType(SlotType.EQUIPMENT, playerId, ItemType.WEAPON).orElse(null);
    }

    private Item getShield(Player player) {
        final long playerId = player.getId();
        return itemRepository.findBySlotTypeAndPlayerIdAndItemType(SlotType.EQUIPMENT, playerId, ItemType.SHIELD).orElse(null);
    }

    private int[] calculateWeaponDamageRange(final Item weapon, final long playerLevel, final Class playerClass) {
        // Αν δεν υπάρχει όπλο, κάνε ένα απλό range με βάση το class & level
        if (weapon == null || weapon.getItemType() != ItemType.WEAPON) {
            int base = switch (playerClass) {
                case WARRIOR -> (int) (playerLevel * 2.0);
                case SCOUT   -> (int) (playerLevel * 1.8);
                case MAGE    -> (int) (playerLevel * 2.2);
            };
            return new int[]{Math.max(1, base - 2), base + 2};
        }

        // 🔸 Rarity multiplier
        double rarityMultiplier = switch (weapon.getItemRarity()) {
            case LEGENDARY -> 1.5;
            case EPIC -> 1.3;
            default -> 1.0;
        };

        // 🔸 Class multiplier – κάθε class έχει διαφορετική base scaling
        double classMultiplier = switch (playerClass) {
            case WARRIOR -> 1.0;
            case SCOUT   -> 0.9;  // scouts βασίζονται σε πιο σταθερό dmg
            case MAGE    -> 1.2;  // mages κάνουν spikes πιο συχνά
        };

        // 🔸 Base damage από το level
        int baseMin = (int) Math.floor(playerLevel * 1.5 * classMultiplier * rarityMultiplier);
        int baseMax = (int) Math.floor(playerLevel * 2.0 * classMultiplier * rarityMultiplier);

        // 🔸 Προσθήκη bonus από τα primary stats του weapon
        int statBonus = Math.max(weapon.getStrength(),
                Math.max(weapon.getDexterity(), weapon.getIntelligence()));
        baseMin += (int) (statBonus * 0.5);
        baseMax += statBonus;

        return new int[]{baseMin, baseMax};
    }


    // PvE (Dungeons, Tavern Quests, Tower
    public void simulatePveFight() {

    }
}
