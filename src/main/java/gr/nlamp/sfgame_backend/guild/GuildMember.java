package gr.nlamp.sfgame_backend.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.nlamp.sfgame_backend.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "guild_members")
@Getter
@Setter
public class GuildMember implements Serializable {
    @EmbeddedId
    GuildMemberPK guildMemberPK = new GuildMemberPK();
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JsonIgnore
    @MapsId("guildId")
    private Guild guild;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JsonIgnore
    @MapsId("playerId")
    private Player player;
    private Rank playerRank;
    private BigInteger silverDonated;
    private BigInteger mushroomDonated;
    private Boolean isActive;
}
