package gr.nlamp.sfgame_backend.item.booster;

import gr.nlamp.sfgame_backend.item.SlotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoosterRepository extends JpaRepository<Booster, Long> {

    @Query("SELECT COUNT(*) FROM Booster b WHERE b.player.id = :playerId AND b.slotType = 'EQUIPMENT'")
    int countActiveBoostersForPlayer(@Param("playerId") final long playerId);

    List<Booster> findByPlayerIdAndSlotType(long playerId, SlotType slotType);

}
