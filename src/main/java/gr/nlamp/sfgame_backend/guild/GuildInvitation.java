package gr.nlamp.sfgame_backend.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.nlamp.sfgame_backend.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "guild_invitations")
@Getter
@Setter
public class GuildInvitation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JsonIgnore
    @JoinColumn(name = "guild_id")
    private Guild guild;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JsonIgnore
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(columnDefinition = "ENUM('ON_HOLD','ACCEPTED','REJECTED'")
    @Enumerated(value = EnumType.STRING)
    private GuildInvitationStatus status;
}
