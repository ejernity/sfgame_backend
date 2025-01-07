package gr.nlamp.sfgame_backend.tavern;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {

    List<Quest> findAllByPlayerId(final long playerId);

    Optional<Quest> findByPlayerIdAndIsChosenEquals(final long playerId, final boolean isChosen);

    @Modifying
    @Query("DELETE FROM Quest q WHERE q.player.id = :playerId")
    void deleteAllByPlayerId(@Param("playerId") final long playerId);

}
