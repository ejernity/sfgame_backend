package gr.nlamp.sfgame_backend.tavern;

import gr.nlamp.sfgame_backend.player.Player;
import gr.nlamp.sfgame_backend.player.PlayerRepository;
import gr.nlamp.sfgame_backend.player.PlayerService;
import gr.nlamp.sfgame_backend.player.PlayerState;
import gr.nlamp.sfgame_backend.stable.MountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final PlayerRepository playerRepository;
    private final QuestGenerator questGenerator;
    private final MountService mountService;
    private final PlayerService playerService;
    private final QuestMapper questMapper = QuestMapper.INSTANCE;

    private static final int BEER_ENERGY = 20;

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public QuestsDto getAll(final long playerId) {
        final Player player = getPlayer(playerId);
        final List<Quest> quests = questRepository.findAllByPlayerId(playerId);
        mountService.clearInactiveMount(player);
        refreshEnergyIfNeeded(player);
        player.setLastTavernAccessDate(System.currentTimeMillis());
        return new QuestsDto(questMapper.mapList(quests));
    }

    private void refreshEnergyIfNeeded(Player player) {
        if (player.getLastTavernAccessDate() == null)
            return;
        final LocalDate now = LocalDate.now();
        final LocalDate lastTavernAccess = Instant.ofEpochMilli(player.getLastTavernAccessDate()).atZone(ZoneId.systemDefault()).toLocalDate();
        if (!now.equals(lastTavernAccess)) {
            player.setCurrentEnergy(BigDecimal.valueOf(100));
            player.setTotalUsedEnergy(BigDecimal.ZERO);
            player.setTotalBeersDrink(0);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void start(final long playerId, final long questId) {
        final Player player = getPlayer(playerId);
        validatePlayerState(player, PlayerState.IDLE);
        final Quest quest = getQuest(player, questId);
        validatePlayerEnergyForQuest(player, quest);
        quest.setIsChosen(true);
        quest.setChosenAt(System.currentTimeMillis());
        player.setPlayerState(PlayerState.QUEST);
    }

    private void validatePlayerEnergyForQuest(final Player player, final Quest quest) {
        final BigDecimal questEnergy = quest.getDuration();
        final BigDecimal playerEnergy = player.getCurrentEnergy();
        if (questEnergy.compareTo(playerEnergy) > 0)
            throw new RuntimeException("Not enough energy.");
    }

    @Transactional(rollbackOn = Exception.class)
    public void cancel(final long playerId) {
        final Player player = getPlayer(playerId);
        validatePlayerState(player, PlayerState.QUEST);
        final Quest quest = getChosenQuest(playerId);
        mountService.clearInactiveMount(player);
        validateQuestForCancel(quest);
        quest.setIsChosen(false);
        quest.setChosenAt(null);
        player.setPlayerState(PlayerState.IDLE);
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public RewardDto finish(final long playerId) {
        final Player player = getPlayer(playerId);
        validatePlayerState(player, PlayerState.QUEST);
        final Quest quest = getChosenQuest(playerId);
        mountService.clearInactiveMount(player);
        validateQuestForFinish(quest);
        player.setPlayerState(PlayerState.IDLE);
        player.setCoins(player.getCoins().add(quest.getCoins()));
        player.setMushrooms(player.getMushrooms() + quest.getMushrooms());
        final BigDecimal mountBooster = player.getMount() != null ? BigDecimal.valueOf(player.getMount().getPercentageBooster()) : BigDecimal.ONE;
        player.setCurrentEnergy(player.getCurrentEnergy().subtract(quest.getDuration().multiply(mountBooster)));
        player.setTotalUsedEnergy(player.getTotalUsedEnergy().add(quest.getDuration().multiply(mountBooster)));
        player.setNumberOfSuccessQuests(player.getNumberOfSuccessQuests() + 1);
        questGenerator.generateQuests(player, false);
        playerService.addExperience(player, quest.getExperience());
        // TODO Add item in bag if quest gives an item.
        //  If not space exists for the item, throw an exception with appropriate error message for the player
        return questMapper.toRewardDto(quest);
    }

    @Transactional(rollbackOn = Exception.class)
    public void drinkBeer(final long playerId) {
        final Player player = getPlayer(playerId);
        validatePlayerState(player, PlayerState.IDLE);
        validatePlayerCanDrinkBeer(player);
        player.setMushrooms(player.getMushrooms() - 1);
        player.setCurrentEnergy(player.getCurrentEnergy().add(BigDecimal.valueOf(BEER_ENERGY)));
        player.setTotalBeersDrink(player.getTotalBeersDrink() + 1);
    }

    private void validatePlayerCanDrinkBeer(final Player player) {
        // player has reached maximum beers for today
        if (player.getTotalBeersDrink() == 10)
            throw new RuntimeException("Player has already drank all beers for today.");
        // player has not enough mushrooms
        if (player.getMushrooms() == 0)
            throw new RuntimeException("Player has no mushrooms to buy a beer.");
        // player has current energy less than or equal to 80 (because each beer gives 20 energy), otherwise error
        if (player.getCurrentEnergy().compareTo(BigDecimal.valueOf(80)) > 0)
            throw new RuntimeException("Player has enough energy to do a quest.");
        // player reached maximum energy for today
        if (player.getTotalUsedEnergy().compareTo(BigDecimal.valueOf(300)) == 0)
            throw new RuntimeException("Player has reached maximum used energy.");
    }

    private void validateQuestForCancel(final Quest quest) {
        if (!quest.getIsChosen())
            throw new RuntimeException("Quest is not chosen, so it cannot be canceled.");
        long durationInMillis = (long) quest.getDuration().toBigInteger().intValue() * 60 * 1000;
        final Player player = quest.getPlayer();
        if (player.getMount() != null)
            durationInMillis = (long) (player.getMount().getPercentageBooster() * durationInMillis);
        if (quest.getChosenAt() + durationInMillis <= System.currentTimeMillis())
            throw new RuntimeException("Quest has finished, so it cannot be canceled.");
    }

    private void validateQuestForFinish(final Quest quest) {
        if (!quest.getIsChosen())
            throw new RuntimeException("Quest is not chosen, so it cannot be finished.");
        long durationInMillis = (long) quest.getDuration().toBigInteger().intValue() * 60 * 1000;
        final Player player = quest.getPlayer();
        if (player.getMount() != null)
            durationInMillis = (long) (player.getMount().getPercentageBooster() * durationInMillis);
        if (quest.getChosenAt() + durationInMillis > System.currentTimeMillis())
            throw new RuntimeException("Quest has not finished yet.");
    }

    private Quest getQuest(final Player player, final long questId) {
        final Optional<Quest> optionalQuest = questRepository.findById(questId);
        if (optionalQuest.isEmpty())
            throw new RuntimeException("Quest not found");

        final Quest quest = optionalQuest.get();
        if (!quest.getPlayer().equals(player))
            throw new RuntimeException("You cannot start quest of another player.");
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
            throw new RuntimeException(String.format("Player is not in state %s", state.name()));
    }
}
