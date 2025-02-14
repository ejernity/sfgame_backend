package gr.nlamp.sfgame_backend.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.nlamp.sfgame_backend.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "guild_members", uniqueConstraints = {
        @UniqueConstraint(name = "UC_player", columnNames = {"player_id"})
})
@Getter
@Setter
public class GuildMember implements Serializable {
    @EmbeddedId
    GuildMemberPK guildMemberPK = new GuildMemberPK();

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JsonIgnore
    @MapsId("guildId")
    @JoinColumn(name = "guild_id")
    private Guild guild;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JsonIgnore
    @MapsId("playerId")
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(columnDefinition = "ENUM('LEADER','OFFICER','MEMBER')")
    @Enumerated(value = EnumType.STRING)
    private Rank playerRank;

    private BigInteger goldDonated;

    private Long mushroomDonated;

}
