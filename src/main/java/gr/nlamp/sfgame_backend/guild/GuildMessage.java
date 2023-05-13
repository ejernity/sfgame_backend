package gr.nlamp.sfgame_backend.guild;

import gr.nlamp.sfgame_backend.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "guild_messages")
@Getter
@Setter
public class GuildMessage implements Serializable {
    @Id
    private UUID id;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Guild guild;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Player player;
    private Long timeStamp;
    @Lob
    private String message;
}
