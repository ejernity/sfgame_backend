package gr.nlamp.sfgame_backend.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.nlamp.sfgame_backend.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "guild_members")
@Getter
@Setter
public class GuildMember implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JsonIgnore
    @MapsId("guildId")
    private Guild guild;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    @JsonIgnore
    @MapsId("playerId")
    private Player player;
    private Rank playerRank;
    private Long silverDonated;
    private Long mushroomDonated;
}
