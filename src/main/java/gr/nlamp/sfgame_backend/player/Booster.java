package gr.nlamp.sfgame_backend.player;

import gr.nlamp.sfgame_backend.item.PotionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "boosters")
@Getter
@Setter
public class Booster implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Player player;
    private PotionType potionType;
    private Long duration;
}
