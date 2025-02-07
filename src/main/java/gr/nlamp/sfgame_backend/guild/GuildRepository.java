package gr.nlamp.sfgame_backend.guild;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuildRepository extends JpaRepository<Guild, Long> {

    @Query("SELECT g " +
            "FROM Guild g " +
            "JOIN FETCH GuildMember gm ON g.id = gm.guild.id " +
            "WHERE gm.player.id = :playerId")
    Guild findGuildForPlayerId(@Param("playerId") final long playerId);

    Optional<Guild> findByName(String name);

}
