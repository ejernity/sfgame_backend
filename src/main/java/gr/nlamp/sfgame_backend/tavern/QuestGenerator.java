package gr.nlamp.sfgame_backend.tavern;

import gr.nlamp.sfgame_backend.guild.Guild;
import gr.nlamp.sfgame_backend.guild.GuildRepository;
import gr.nlamp.sfgame_backend.player.Player;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class QuestGenerator {

    private final GuildRepository guildRepository;
    private final QuestRepository questRepository;

    private static final int[] DURATIONS = {20, 15, 10, 5}; // Quest durations in minutes
    private static final double MUSHROOM_REWARD_CHANCE = 0.05; // 5% chance to reward 1 mushroom

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRED)
    public void generateQuests(final Player player, final boolean isRegistrationRequest) {
        // Clear player's existing quests
        if (!isRegistrationRequest) {
            player.getQuests().clear();
            questRepository.deleteAllByPlayerId(player.getId());
        }

        final long level = player.getLevel();
        final Guild guild = isRegistrationRequest ? null : guildRepository.findGuildForPlayerId(player.getId());
        Integer treasureLevel = 0;
        Integer instructorLevel = 0;
        if (guild != null) {
            treasureLevel = guild.getTreasureLevel();
            instructorLevel = guild.getInstructorLevel();
        }

        final List<Quest> newQuests = new ArrayList<>();
        final Random random = new Random();

        for (short questNumber = 1; questNumber <= 3; questNumber++) {
            final Quest quest = new Quest();
            quest.setOrderNo(questNumber);

            // Random decision for the quest duration
            final BigDecimal duration = BigDecimal.valueOf(DURATIONS[random.nextInt(DURATIONS.length)]);
            quest.setDuration(duration);

            // Coins calculation with randomness
            final double coinMultiplier = 1 + (0.02 * treasureLevel);
            final double randomCoinFactor = 0.9 + (0.2 * random.nextDouble()); // Range [0.9, 1.1]
            final BigInteger coins =
                    duration.multiply(duration)
                            .multiply(BigDecimal.valueOf(level * coinMultiplier * randomCoinFactor))
                            .toBigInteger();
            quest.setCoins(coins);

            // Experience calculation with randomness
            final double experienceMultiplier = 1 + (0.02 * instructorLevel);
            final double randomExperienceFactor = 0.85 + (0.3 * random.nextDouble()); // Range [0.85, 1.15]
            final BigInteger experience =
                    duration.multiply(duration)
                            .multiply(BigDecimal.valueOf(level * experienceMultiplier * randomExperienceFactor))
                            .toBigInteger();
            quest.setExperience(experience);

            // Random number of mushrooms as a reward
            quest.setMushrooms(random.nextDouble() < MUSHROOM_REWARD_CHANCE ? 1L : 0L);

            // TODO Randomly generate item as a reward
            //  If condition meet, then give parts of the magic mirror instead of items to equip
            //  If condition meet, then give dungeon key instead of classic item

            quest.setPlayer(player);

            // Add the quest to the list
            newQuests.add(quest);
        }

        // Add all new quests to the player's quests
        player.getQuests().addAll(newQuests);
    }
}
