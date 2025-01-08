package gr.nlamp.sfgame_backend.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Modifying
    @Query("DELETE FROM Item i WHERE i.player.id = :playerId AND i.slotType IN :slotTypes")
    void deleteItemsByPlayerAndSlotTypes(@Param("playerId") final long playerId,
                                         @Param("slotTypes") final List<SlotType> slotTypes);

    Optional<Item> findBySlotTypeAndPlayerId(final SlotType slotType, final long playerId);

}

