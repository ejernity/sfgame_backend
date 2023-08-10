package gr.nlamp.sfgame_backend.guild;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
@Getter
@Setter
public class GuildMemberPK implements Serializable {
    protected Long guildId;
    protected Long playerId;
}