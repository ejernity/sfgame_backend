package gr.nlamp.sfgame_backend.tavern;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
@Getter
@Setter
public class QuestPK implements Serializable {
    protected Long playerId;
    protected Short orderNo;
}
