package gr.nlamp.sfgame_backend.player;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByUsername(final String username);

    Player findByEmail(final String email);

    @Query("SELECT p FROM Player p")
    Slice<Player> findPlayers(Pageable pageable);

}
