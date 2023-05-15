package gr.nlamp.sfgame_backend.tavern;

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
public class QuestPK implements Serializable {
    protected UUID playerId;
    protected Short orderNo;
}
