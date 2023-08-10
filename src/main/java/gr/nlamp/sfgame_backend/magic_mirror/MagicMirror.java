package gr.nlamp.sfgame_backend.magic_mirror;

import gr.nlamp.sfgame_backend.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "magic_mirrors")
@Getter
@Setter
public class MagicMirror implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false, unique = true)
    private Player player;
    private Boolean shardOfClumsiness;
    private Boolean shardOfMultitaskers;
    private Boolean shardOfDoubleStandards;
    private Boolean shardOfSelfReflection;
    private Boolean shardOfPresumption;
    private Boolean shardOfTwilight;
    private Boolean shardOfPrudence;
    private Boolean shardOfVanity;
    private Boolean shardOfDoubling;
    private Boolean shardOfMisfortune;
    private Boolean shardOfReplication;
    private Boolean shardOfDoppelgangers;
    private Boolean shardOfCopyrightIssues;

}
