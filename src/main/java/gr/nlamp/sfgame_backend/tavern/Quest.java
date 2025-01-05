package gr.nlamp.sfgame_backend.tavern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.nlamp.sfgame_backend.item.Item;
import gr.nlamp.sfgame_backend.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "quests")
@Getter
@Setter
public class Quest implements Serializable {
    @EmbeddedId
    QuestPK questPK = new QuestPK();

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JsonIgnore
    @MapsId("playerId")
    private Player player;

    private Integer duration;

    private BigInteger coins;

    private BigInteger experience;

    private Boolean itemReward = false;

    private Long mushrooms;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;
}
