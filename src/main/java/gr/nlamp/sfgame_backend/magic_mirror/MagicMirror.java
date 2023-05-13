package gr.nlamp.sfgame_backend.magic_mirror;

import gr.nlamp.sfgame_backend.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "magic_mirrors")
@Getter
@Setter
public class MagicMirror implements Serializable {
    @Id
    private UUID id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
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
