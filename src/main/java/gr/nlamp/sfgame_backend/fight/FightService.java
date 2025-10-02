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
        boolean attackerTurn = new Random().nextBoolean();

        while (playerAlive && targetPlayerAlive) {

            if (attackerTurn) {
                final AttackResultDto turnResult = performAttack(
                        attacker, defender,
                        attackerWeapon, defenderWeapon,
                        attackerShield, defenderShield,
                        attackerSkills, defenderSkills
                );
                result.add(turnResult);
            } else {
                final AttackResultDto turnResult = performAttack(
                        defender, attacker,
                        defenderWeapon, attackerWeapon,
                        defenderShield, attackerShield,
                        defenderSkills, attackerSkills
                );
                result.add(turnResult);
            }

            playerAlive = attackerSkills.getHitPoints().compareTo(BigInteger.ZERO) > 0;
            targetPlayerAlive = defenderSkills.getHitPoints().compareTo(BigInteger.ZERO) > 0;

            attackerTurn = !attackerTurn;
        }

        return result;
    }

    private AttackResultDto performAttack(Player attacker, Player defender,
                                          Item attackerWeapon, Item defenderWeapon,
                                          Item attackerShield, Item defenderShield,
                                          SkillsAnalyticalDto attackerSkills, SkillsAnalyticalDto defenderSkills) {
        final Random rnd = new Random();

        // 1. Βρες damage source
        int baseDamage = switch (attacker.getPlayerClass()) {
            case WARRIOR -> attacker.getStrength().intValue();
            case SCOUT  -> attacker.getDexterity().intValue();
            case MAGE    -> attacker.getIntelligence().intValue();
        };

        if (attackerWeapon != null) {
            baseDamage *= rnd.nextInt(1, 5);
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
        if (defender.getPlayerClass() == Class.WARRIOR) {
            blocked = rnd.nextDouble() < defender.getConstitution().doubleValue() / 300.0;
        }

        if (dodged) {
            return new AttackResultDto(attacker.getId(), defender.getId(), 0, true, false, critical, defenderSkills.getHitPoints());
        }
        if (blocked) {
            baseDamage = (int) (baseDamage * 0.5); // μειώνεται στο μισό
        }

        // 4. Armor mitigation
        int finalDamage = baseDamage - defenderSkills.getTotalArmor();
        if (finalDamage < 0) finalDamage = 0;

        // 5. Reduce defender hit points
        defenderSkills.setHitPoints(defenderSkills.getHitPoints().subtract(BigInteger.valueOf(finalDamage)));

        return new AttackResultDto(attacker.getId(), defender.getId(), finalDamage, false, blocked, critical, defenderSkills.getHitPoints());
    }

    private Item getWeapon(Player player) {
        final long playerId = player.getId();
        return itemRepository.findBySlotTypeAndPlayerIdAndItemType(SlotType.EQUIPMENT, playerId, ItemType.WEAPON).orElse(null);
    }

    private Item getShield(Player player) {
        final long playerId = player.getId();
        return itemRepository.findBySlotTypeAndPlayerIdAndItemType(SlotType.EQUIPMENT, playerId, ItemType.SHIELD).orElse(null);
    }

    // PvE (Dungeons, Tavern Quests, Tower
    public void simulatePveFight() {

    }
}
