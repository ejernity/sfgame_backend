package gr.nlamp.sfgame_backend.player;

import gr.nlamp.sfgame_backend.item.PotionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "boosters")
@Getter
@Setter
public class Booster implements Serializable {
    @Id
    private UUID id;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Player player;
    private PotionType potionType;
    private Long duration;
}
