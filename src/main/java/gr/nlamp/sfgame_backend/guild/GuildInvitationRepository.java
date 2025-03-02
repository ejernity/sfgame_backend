package gr.nlamp.sfgame_backend.guild;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuildInvitationRepository extends JpaRepository<GuildInvitation, Long> {

    Optional<GuildInvitation> findByPlayerIdAndGuildIdAndStatus(long playerId, long guildId, GuildInvitationStatus status);

    List<GuildInvitation> findByPlayerIdOrderByIdAsc(long playerId);

}
