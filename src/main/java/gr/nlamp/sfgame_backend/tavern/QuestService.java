package gr.nlamp.sfgame_backend.tavern;

import gr.nlamp.sfgame_backend.player.Player;
import gr.nlamp.sfgame_backend.player.PlayerRepository;
import gr.nlamp.sfgame_backend.player.PlayerState;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final PlayerRepository playerRepository;
    private final QuestGenerator questGenerator;
    private final QuestMapper questMapper = QuestMapper.INSTANCE;

    public QuestsDto getAll(final long playerId) {
        final List<Quest> quests = questRepository.findAllByPlayerId(playerId);
        return new QuestsDto(questMapper.mapList(quests));
    }

    @Transactional(rollbackOn = Exception.class)
    public void start(final long playerId, final short orderNo) {
        final Player player = getPlayer(playerId);
        validatePlayerState(player, PlayerState.IDLE);
        final Quest quest = getQuest(playerId, orderNo);
        quest.setIsChosen(true);
        quest.setChosenAt(System.currentTimeMillis());
        player.setPlayerState(PlayerState.QUEST);
    }

    @Transactional(rollbackOn = Exception.class)
    public void cancel(final long playerId) {
        final Player player = getPlayer(playerId);
        validatePlayerState(player, PlayerState.QUEST);
        final Quest quest = getChosenQuest(playerId);
        validateQuestForCancel(quest);
        quest.setIsChosen(false);
        quest.setChosenAt(null);
        player.setPlayerState(PlayerState.IDLE);
    }

    @Transactional(rollbackOn = Exception.class)
    public RewardDto finish(final long playerId) {
        final Player player = getPlayer(playerId);
        validatePlayerState(player, PlayerState.QUEST);
        final Quest quest = getChosenQuest(playerId);
        validateQuestForFinish(quest);
        player.setPlayerState(PlayerState.IDLE);
        player.setCoins(player.getCoins().add(quest.getCoins()));
        player.setMushrooms(player.getMushrooms() + quest.getMushrooms());
        player.setGainedExperience(player.getGainedExperience().add(quest.getExperience()));
        questGenerator.generateQuests(player, false);
        // TODO check for level up
        // TODO Add item in bag if quest gives an item.
        //  If not space exists for the item, throw an exception with appropriate error message for the player
        return questMapper.toRewardDto(quest);
    }

    private void validateQuestForCancel(final Quest quest) {
        if (!quest.getIsChosen())
            throw new RuntimeException("Quest is not chosen, so it cannot be canceled.");
        final long durationInMillis = quest.getDuration() * 60 * 1000;
        if (quest.getChosenAt() + durationInMillis <= System.currentTimeMillis())
            throw new RuntimeException("Quest has finished, so it cannot be canceled.");
    }

    private void validateQuestForFinish(final Quest quest) {
        if (!quest.getIsChosen())
            throw new RuntimeException("Quest is not chosen, so it cannot be finished.");
        final long durationInMillis = quest.getDuration() * 60 * 1000;
        if (quest.getChosenAt() + durationInMillis > System.currentTimeMillis())
            throw new RuntimeException("Quest has not finished yet.");
    }

    private Quest getQuest(final long playerId, final short orderNo) {
        final Optional<Quest> optionalQuest = questRepository.findByPlayerIdAndOrderNo(playerId, orderNo);
        if (optionalQuest.isEmpty())
            throw new RuntimeException("Quest not found");

        return optionalQuest.get();
    }

    private Quest getChosenQuest(final long playerId) {
        final Optional<Quest> optionalQuest = questRepository.findByPlayerIdAndIsChosenEquals(playerId, true);
        if (optionalQuest.isEmpty())
            throw new RuntimeException("Quest not found");

        return optionalQuest.get();
    }

    private Player getPlayer(final long playerId) {
        final Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null)
            throw new RuntimeException("Player not found");
        return player;
    }

    private void validatePlayerState(final Player player, final PlayerState state) {
        if (!player.getPlayerState().equals(state))
            throw new RuntimeException("Player is not idle");
    }

}
