package gr.nlamp.sfgame_backend.guild;

import gr.nlamp.sfgame_backend.guild.dto.CreateGuildDto;
import gr.nlamp.sfgame_backend.guild.dto.GuildInvitationDto;
import gr.nlamp.sfgame_backend.guild.dto.ProcessGuildInvitationDto;
import gr.nlamp.sfgame_backend.player.Player;
import gr.nlamp.sfgame_backend.player.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    private static final BigInteger COINS_TO_CREATE_GUILD = BigInteger.valueOf(10);

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
        member.setSilverDonated(BigInteger.ZERO);
        member.setMushroomDonated(BigInteger.ZERO);
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
