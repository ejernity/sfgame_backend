package gr.nlamp.sfgame_backend.guild;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GuildMessageRepository extends JpaRepository<GuildMessage, Long> {

    @EntityGraph(attributePaths = {"player"})
    Set<GuildMessage> findByGuildIdOrderByTimeStampAsc(final long guildId);

}
