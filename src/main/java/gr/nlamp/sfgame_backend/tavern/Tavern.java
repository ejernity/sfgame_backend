package gr.nlamp.sfgame_backend.tavern;

import gr.nlamp.sfgame_backend.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.UUID;

@Entity
@Table(name = "tavern")
@Getter
@Setter
public class Tavern implements Serializable {
    @Id
    private UUID id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Player player;
    private Integer thirst; // renew every day 6000 (max=6000)
    private Short beers; // min=0, max=10
    private Integer questDuration_1;
    private BigInteger questSilver_1;
    private BigInteger questExperience_1;
    private Integer questDuration_2;
    private BigInteger questSilver_2;
    private BigInteger questExperience_2;
    private Integer questDuration_3;
    private BigInteger questSilver_3;
    private BigInteger questExperience_3;
}
