package gr.nlamp.sfgame_backend.tavern;

import gr.nlamp.sfgame_backend.guild.Guild;
import gr.nlamp.sfgame_backend.guild.GuildRepository;
import gr.nlamp.sfgame_backend.player.Player;
import gr.nlamp.sfgame_backend.player.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestsGenerator {

    private final PlayerRepository playerRepository;
    private final GuildRepository guildRepository;

    public void generateQuests(final Player player) {
        final long level = player.getLevel();
        final Guild guild = guildRepository.findGuildForPlayerId(player.getId());
        Integer treasureLevel = 0;
        Integer instructorLevel = 0;
        if (guild != null) {
            treasureLevel = guild.getTreasureLevel();
            instructorLevel = guild.getInstructorLevel();
        }

        final List<Quest> newQuests = new ArrayList<>();

        for (int questNumber = 1; questNumber <= 3; questNumber++) {
            final Quest quest = new Quest();
            // random decision of the duration of the quest in seconds between 2,5 minutes, 5 minutes, 7,5 minutes, 10 minutes.
            // coins are calculated depend on the duration, the player's level, the treasure level (2% * treasureLevel)
            // experience is calculated depend on the player's level, the instructor level (2% * instructor level)
            // random number of mushrooms as reward (5% win 1 mushroom)
            // random decision of the existence of an item as a reward -> we will use ItemGenerator given the Player object

            // create unique key for quest and assign it
            // add quest to the newQuests list
        }

        // Clear player's quests
        // Add all newQuests to player's quests
    }
}
