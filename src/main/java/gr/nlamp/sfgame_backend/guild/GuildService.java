package gr.nlamp.sfgame_backend.guild;

import gr.nlamp.sfgame_backend.guild.dto.CreateGuildDto;
import gr.nlamp.sfgame_backend.player.Player;
import gr.nlamp.sfgame_backend.player.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuildService {

    private final GuildRepository guildRepository;
    private final PlayerRepository playerRepository;

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
