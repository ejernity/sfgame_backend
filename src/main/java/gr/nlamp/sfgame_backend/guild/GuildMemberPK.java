package gr.nlamp.sfgame_backend.guild;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
@Getter
@Setter
public class GuildMemberPK implements Serializable {

    protected UUID guildId;
    protected UUID playerId;

}