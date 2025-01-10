package gr.nlamp.sfgame_backend.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoosterRepository extends JpaRepository<Booster, Long> {

    @Query("SELECT COUNT(*) FROM Booster b WHERE b.player.id = :playerId AND b.slotType = 'EQUIPMENT'")
    int countActiveBoostersForPlayer(@Param("playerId") final long playerId);

}
