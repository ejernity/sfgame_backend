package gr.nlamp.sfgame_backend.guild;

import gr.nlamp.sfgame_backend.guild.dto.*;
import gr.nlamp.sfgame_backend.player.Player;
import gr.nlamp.sfgame_backend.player.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GuildService {

    private final GuildRepository guildRepository;
    private final PlayerRepository playerRepository;
    private final GuildInvitationRepository guildInvitationRepository;
    private final GuildMemberRepository guildMemberRepository;
    private final GuildMessageRepository guildMessageRepository;

    private final GuildMessageMapper guildMessageMapper = GuildMessageMapper.INSTANCE;

    private static final BigInteger COINS_TO_CREATE_GUILD = BigInteger.valueOf(10);
    private static final int MAX_TREASURE_INSTRUCTOR_LEVEL = 25;

    private final MessageSource messageSource;

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public void create(final CreateGuildDto dto, final long playerId) {
        final Player player = getPlayer(playerId);
        validatePlayerDoNotBelongToAGuild(player);
        validatePlayerHasEnoughCoinsToCreateGuild(player);
        validateGuildNameIsNotReserved(dto.getName());

        final Guild guild = createGuild(dto);
        createGuildMemberForPlayer(guild, player, Rank.LEADER);

        player.setCoins(player.getCoins().subtract(COINS_TO_CREATE_GUILD));
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRED)
    protected Guild createGuild(CreateGuildDto dto) {
        final Guild guild = new Guild();
        guild.setName(dto.getName());
        guild.setHonor(BigInteger.ZERO);
        guild.setGold(BigInteger.ZERO);
        guild.setMushrooms(0L);
        guild.setInstructorLevel(0);
        guild.setTreasureLevel(0);
        return guildRepository.save(guild);
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRED)
    protected void createGuildMemberForPlayer(Guild guild, Player player, Rank rank) {
        final GuildMemberPK pk = new GuildMemberPK();
        pk.setPlayerId(player.getId());
        pk.setGuildId(guild.getId());
        final GuildMember member = new GuildMember();
        member.setGuild(guild);
        guild.getGuildMembers().add(member);
        member.setGuildMemberPK(pk);
        member.setPlayer(player);
        player.getGuildMembers().add(member);
        member.setPlayerRank(rank);
        member.setGoldDonated(BigInteger.ZERO);
        member.setMushroomDonated(0L);
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public void invite(final GuildInvitationDto dto, final long playerId) {
        final Player playerToInvite = getPlayer(dto.getPlayerId());
        final Player player = getPlayer(playerId);
        final GuildMember guildMember = getGuildMember(player);
        validatePlayerRankCanSendInvitation(guildMember);

        final Guild guild = guildMember.getGuild();
        validatePlayerToInviteIsNotAnExistingGuildMember(guild, playerToInvite);
        validateNoExistingGuildInvitationForPlayerAndGuildAndStatusOnHold(playerToInvite, guild);
        validateGuildHasEnoughSpaceToInvitePlayer(guild);

        final GuildInvitation guildInvitation = new GuildInvitation();
        guildInvitation.setGuild(guild);
        guildInvitation.setPlayer(playerToInvite);
        guildInvitation.setStatus(GuildInvitationStatus.ON_HOLD);
        guildInvitationRepository.save(guildInvitation);
        // TODO Create system message to the playerToInvite for the invitation
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public void acceptInvitation(final ProcessGuildInvitationDto dto, final long playerId) {
        final Player player = getPlayer(playerId);
        validatePlayerDoNotBelongToAGuild(player);

        final GuildInvitation invitation = getInvitationIfExists(dto, playerId);
        invitation.setStatus(GuildInvitationStatus.ACCEPTED);

        createGuildMemberForPlayer(invitation.getGuild(), player, Rank.MEMBER);
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public void rejectInvitation(final ProcessGuildInvitationDto dto, final long playerId) {
        final GuildInvitation invitation = getInvitationIfExists(dto, playerId);
        invitation.setStatus(GuildInvitationStatus.REJECTED);
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public void upgradeTreasure(final long playerId) {
        final Player player = getPlayer(playerId);
        final GuildMember guildMember = getGuildMember(player);
        validatePlayerRankCanUpgradeTreasureAndInstructor(guildMember);

        final Guild guild = guildMember.getGuild();
        validateMaxLevelNotYetReached(guild, UpgradeType.TREASURE);
        final TreasureInstructorUpgradeCostsDto upgradeCostsDto = validateGuildHasEnoughResourcesToDoUpgrade(guild, UpgradeType.TREASURE);

        guild.setGold(guild.getGold().subtract(upgradeCostsDto.getGold()));
        guild.setMushrooms(guild.getMushrooms() - upgradeCostsDto.getMushrooms());
        guild.setTreasureLevel(guild.getTreasureLevel() + 1);

        final GuildMessage guildMessage = new GuildMessage();
        guildMessage.setGuild(guild);
        guildMessage.setMessage(messageSource.getMessage("guild.upgrade.treasure", new String[]{player.getUsername(), guild.getTreasureLevel().toString()}, LocaleContextHolder.getLocale()));
        guildMessageRepository.save(guildMessage);
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public void upgradeInstructor(long playerId) {
        final Player player = getPlayer(playerId);
        final GuildMember guildMember = getGuildMember(player);
        validatePlayerRankCanUpgradeTreasureAndInstructor(guildMember);

        final Guild guild = guildMember.getGuild();
        validateMaxLevelNotYetReached(guild, UpgradeType.INSTRUCTOR);
        final TreasureInstructorUpgradeCostsDto upgradeCostsDto = validateGuildHasEnoughResourcesToDoUpgrade(guild, UpgradeType.INSTRUCTOR);

        guild.setGold(guild.getGold().subtract(upgradeCostsDto.getGold()));
        guild.setMushrooms(guild.getMushrooms() - upgradeCostsDto.getMushrooms());
        guild.setInstructorLevel(guild.getInstructorLevel() + 1);

        final GuildMessage guildMessage = new GuildMessage();
        guildMessage.setGuild(guild);
        guildMessage.setMessage(messageSource.getMessage("guild.upgrade.instructor", new String[]{player.getUsername(), guild.getInstructorLevel().toString()}, LocaleContextHolder.getLocale()));
        guildMessageRepository.save(guildMessage);
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public void donateGold(final DonateQuantityDto dto, final long playerId) {
        validateDonateQuantityDto(dto, DonationType.GOLD);
        final Player player = getPlayer(playerId);
        final GuildMember guildMember = getGuildMember(player);
        validatePlayerHasEnoughResourcesToDonate(player, DonationType.GOLD, dto);
        final Guild guild = guildMember.getGuild();

        player.setCoins(player.getCoins().subtract(dto.getGold()));
        guild.setGold(guild.getGold().add(dto.getGold()));

        final GuildMessage guildMessage = new GuildMessage();
        guildMessage.setGuild(guild);
        guildMessage.setMessage(messageSource.getMessage("guild.donation.gold", new String[]{player.getUsername(), dto.getGold().toString()}, LocaleContextHolder.getLocale()));
        guildMessageRepository.save(guildMessage);
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public void donateMushrooms(final DonateQuantityDto dto, final long playerId) {
        validateDonateQuantityDto(dto, DonationType.MUSHROOMS);
        final Player player = getPlayer(playerId);
        final GuildMember guildMember = getGuildMember(player);
        validatePlayerHasEnoughResourcesToDonate(player, DonationType.MUSHROOMS, dto);
        final Guild guild = guildMember.getGuild();

        player.setMushrooms(player.getMushrooms() - dto.getMushrooms());
        guild.setMushrooms(guild.getMushrooms() + dto.getMushrooms());

        final GuildMessage guildMessage = new GuildMessage();
        guildMessage.setGuild(guild);
        guildMessage.setMessage(messageSource.getMessage("guild.donation.mushrooms", new String[]{player.getUsername(), String.valueOf(dto.getMushrooms())}, LocaleContextHolder.getLocale()));
        guildMessageRepository.save(guildMessage);
    }

    public GuildMessagesDto getMessages(final long playerId) {
        final Guild guild = guildRepository.findGuildForPlayerId(playerId);
        if (guild == null)
            throw new RuntimeException("Player do not belong to a guild.");

        final Set<GuildMessage> guildMessageSet = guildMessageRepository.findByGuildIdOrderByTimeStampAsc(guild.getId());
        return new GuildMessagesDto(guildMessageMapper.mapSet(guildMessageSet));
    }

    private void validateDonateQuantityDto(DonateQuantityDto dto, DonationType donationType) {
        if (donationType == DonationType.GOLD && (dto.getGold() == null || dto.getGold().compareTo(BigInteger.ZERO) <= 0))
            throw new RuntimeException("Gold to be donated has to be greater than 0");
        if (donationType == DonationType.MUSHROOMS && dto.getMushrooms() <= 0)
            throw new RuntimeException("Mushrooms to be donated has to be greater than 0");
    }

    private void validatePlayerHasEnoughResourcesToDonate(Player player, DonationType donationType, DonateQuantityDto dto) {
        if (donationType == DonationType.GOLD) {
            if (player.getCoins().compareTo(dto.getGold()) < 0)
                throw new RuntimeException("You do not have enough coins to donate.");
        } else {
            if (player.getMushrooms() < dto.getMushrooms())
                throw new RuntimeException("You do not have enough mushrooms to donate.");
        }
    }

    private void validateMaxLevelNotYetReached(Guild guild, UpgradeType upgradeType) {
        Integer level;
        if (upgradeType == UpgradeType.TREASURE)
            level = guild.getTreasureLevel();
        else
            level = guild.getInstructorLevel();

        if (level >= MAX_TREASURE_INSTRUCTOR_LEVEL)
            throw new RuntimeException("Already reached the maximum level for " + upgradeType);
    }

    private TreasureInstructorUpgradeCostsDto validateGuildHasEnoughResourcesToDoUpgrade(Guild guild, final UpgradeType upgradeType) {
        Integer level;
        if (upgradeType == UpgradeType.TREASURE)
            level = guild.getTreasureLevel();
        else
            level = guild.getInstructorLevel();

        final TreasureInstructorUpgradeCostsDto upgradeCostsDto = GuildConstants.getUpgradeCost(level + 1);
        if (upgradeCostsDto.getGold().compareTo(guild.getGold()) > 0 ||
            (upgradeCostsDto.getMushrooms() != 0 && upgradeCostsDto.getMushrooms() > guild.getMushrooms())) {
            throw new RuntimeException("Not enough resources to do upgrade for " + upgradeType + " to level " + (level + 1));
        }
        return upgradeCostsDto;
    }

    private void validatePlayerToInviteIsNotAnExistingGuildMember(Guild guild, Player playerToInvite) {
        final GuildMemberPK pk = new GuildMemberPK();
        pk.setPlayerId(playerToInvite.getId());
        pk.setGuildId(guild.getId());
        final Optional<GuildMember> optionalGuildMember = guildMemberRepository.findById(pk);
        if (optionalGuildMember.isPresent()) {
            throw new RuntimeException("You cannot invite an existing member of your guild");
        }
    }

    private GuildInvitation getInvitationIfExists(ProcessGuildInvitationDto dto, long playerId) {
        final Optional<GuildInvitation> optionalGuildInvitation =
                guildInvitationRepository.findByPlayerIdAndGuildIdAndStatus(playerId, dto.getGuildId(), GuildInvitationStatus.ON_HOLD);
        if (optionalGuildInvitation.isEmpty()) {
            throw new RuntimeException("There is no such an invitation to process.");
        }
        return optionalGuildInvitation.get();
    }

    private void validateGuildHasEnoughSpaceToInvitePlayer(Guild guild) {
        if (guildMemberRepository.countGuildMembersByGuildId(guild.getId()) >= GuildConstants.MAX_MEMBERS) {
            throw new RuntimeException("You have reached the maximum number of members in this guild.");
        }
    }

    private void validateNoExistingGuildInvitationForPlayerAndGuildAndStatusOnHold(Player playerToInvite, Guild guild) {
        final Optional<GuildInvitation> optionalGuildInvitation = guildInvitationRepository.findByPlayerIdAndGuildIdAndStatus(playerToInvite.getId(), guild.getId(), GuildInvitationStatus.ON_HOLD);
        if (optionalGuildInvitation.isPresent()) {
            throw new RuntimeException("There is already an invitation with status on hold for the player and the guild.");
        }
    }

    private static void validatePlayerRankCanSendInvitation(GuildMember guildMember) {
        if (!Rank.CAN_SEND_INVITATION.contains(guildMember.getPlayerRank())) {
            throw new RuntimeException("Can't send invitation.");
        }
    }

    private static void validatePlayerRankCanUpgradeTreasureAndInstructor(GuildMember guildMember) {
        if (!Rank.CAN_UPGRADE_TREASURE_AND_INSTRUCTOR.contains(guildMember.getPlayerRank())) {
            throw new RuntimeException("Can't upgrade treasure and instructor.");
        }
    }

    private GuildMember getGuildMember(Player player) {
        final Set<GuildMember> guildMemberSet = player.getGuildMembers();
        if (guildMemberSet.isEmpty()) {
            throw new RuntimeException("Player do not belong to a guild.");
        }
        return guildMemberSet.iterator().next();
    }

    private void validatePlayerDoNotBelongToAGuild(Player player) {
        final Guild guild = guildRepository.findGuildForPlayerId(player.getId());
        if (guild != null)
            throw new RuntimeException("Player " + player.getId() + " is already in guild");
    }

    private void validatePlayerHasEnoughCoinsToCreateGuild(Player player) {
        if (player.getCoins().compareTo(COINS_TO_CREATE_GUILD) < 0)
            throw new RuntimeException("Player has not enough coins to create guild");
    }

    private void validateGuildNameIsNotReserved(String name) {
        final Optional<Guild> optionalGuild = guildRepository.findByName(name);
        if (optionalGuild.isPresent())
            throw new RuntimeException("Guild name " + name + " is already in use");
    }

    private Player getPlayer(final long playerId) {
        final Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null)
            throw new RuntimeException("Player not found");
        return player;
    }
}
