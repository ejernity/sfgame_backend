package gr.nlamp.sfgame_backend.guild;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildMemberRepository extends JpaRepository<GuildMember, GuildMemberPK> {

    int countGuildMembersByGuildId(long guildId);

    @Modifying
    @Query("DELETE FROM GuildMember WHERE player.id = :playerId")
    void deleteByPlayerId(@Param("playerId") long playerId);
}
