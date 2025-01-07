package gr.nlamp.sfgame_backend.stable;

import gr.nlamp.sfgame_backend.player.Player;
import gr.nlamp.sfgame_backend.player.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class MountService {

    private final PlayerRepository playerRepository;

    @Transactional(rollbackOn = Exception.class)
    public void buyMount(final long playerId, final Mount mount) {
        final Player player = getPlayer(playerId);
        clearInactiveMount(player);
        if (player.getCoins().compareTo(BigInteger.valueOf(mount.getCoinCost())) < 0 || mount.getMushCost() > player.getMushrooms())
            throw new RuntimeException("Player do not have enough resources to buy the mount.");
        final long mountDurationInMs = mount.getDuration() * 86400000;
        if (player.getMount() != null && mount.equals(player.getMount()))
            player.setMountActiveUntil(player.getMountActiveUntil() + mountDurationInMs);
        else {
            player.setMount(mount);
            player.setMountActiveUntil(System.currentTimeMillis() + mountDurationInMs);
        }
        player.setCoins(player.getCoins().subtract(BigInteger.valueOf(mount.getCoinCost())));
        player.setMushrooms(player.getMushrooms() - mount.getMushCost());
    }

    private Player getPlayer(final long playerId) {
        final Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null)
            throw new RuntimeException("Player not found");
        return player;
    }

    @Transactional(rollbackOn = Exception.class)
    public void clearInactiveMount(final Player player) {
        if (player.getMount() != null && player.getMountActiveUntil() < System.currentTimeMillis()) {
            player.setMount(null);
            player.setMountActiveUntil(null);
            playerRepository.save(player);
        }
    }
}
