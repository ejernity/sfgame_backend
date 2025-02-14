package gr.nlamp.sfgame_backend.guild;

import gr.nlamp.sfgame_backend.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "guild_messages")
@Getter
@Setter
public class GuildMessage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Guild guild;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Player player;

    private Long timeStamp;

    @Column(columnDefinition = "TEXT")
    private String message;

    @PrePersist
    private void prePersist() {
        timeStamp = System.currentTimeMillis();
    }
}
