package gr.nlamp.sfgame_backend.guild;

import gr.nlamp.sfgame_backend.guild.dto.CreateGuildDto;
import gr.nlamp.sfgame_backend.guild.dto.GuildInvitationDto;
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
        createGuildMemberForPlayerAsLeader(playerId, guild, player);

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
    protected void createGuildMemberForPlayerAsLeader(long playerId, Guild guild, Player player) {
        final GuildMemberPK pk = new GuildMemberPK();
        pk.setPlayerId(playerId);
        pk.setGuildId(guild.getId());
        final GuildMember member = new GuildMember();
        member.setGuild(guild);
        guild.getGuildMembers().add(member);
        member.setGuildMemberPK(pk);
        member.setPlayer(player);
        player.getGuildMembers().add(member);
        member.setPlayerRank(Rank.LEADER);
        member.setSilverDonated(BigInteger.ZERO);
        member.setMushroomDonated(BigInteger.ZERO);
    }

    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRES_NEW)
    public void invite(final GuildInvitationDto dto, final long playerId) {
        final Player playerToInvite = validateDestinationPlayerExistsAndIsNotTheSameAsAuthentication(playerId, dto);
        final Player player = getPlayer(playerId);
        final GuildMember guildMember = getGuildMember(player);
        validatePlayerRankCanSendInvitation(guildMember);

        final Guild guild = guildMember.getGuild();
        validateGuildHasEnoughSpaceToInvitePlayer(guild);
        validateNoExistingGuildInvitationForPlayerAndGuildAndStatusOnHold(playerToInvite, guild);

        final GuildInvitation guildInvitation = new GuildInvitation();
        guildInvitation.setGuild(guild);
        guildInvitation.setPlayer(playerToInvite);
        guildInvitation.setStatus(GuildInvitationStatus.ON_HOLD);
        guildInvitationRepository.save(guildInvitation);
        // TODO Create system message to the playerToInvite for the invitation
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

    private Player validateDestinationPlayerExistsAndIsNotTheSameAsAuthentication(long playerId, GuildInvitationDto dto) {
        if (dto.getPlayerId() == playerId) {
            throw new RuntimeException("You cannot invite yourself");
        }
        return getPlayer(dto.getPlayerId());
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
