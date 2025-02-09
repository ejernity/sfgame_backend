package gr.nlamp.sfgame_backend.guild;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildMemberRepository extends JpaRepository<GuildMember, GuildMemberPK> {

    int countGuildMembersByGuildId(long guildId);

}
