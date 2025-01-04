package gr.nlamp.sfgame_backend.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByUsername(final String username);

    Player findByEmail(final String email);

}
