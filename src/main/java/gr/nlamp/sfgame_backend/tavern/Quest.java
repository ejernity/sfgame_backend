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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Player player;

    // TODO Add random title and description

    private Short orderNo;

    private Integer duration;

    private BigInteger coins;

    private BigInteger experience;

    private Boolean hasItemReward = false;

    private Long mushrooms;

    private Boolean isChosen = false;

    private Long chosenAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;
}
