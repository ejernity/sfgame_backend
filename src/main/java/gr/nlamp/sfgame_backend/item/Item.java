package gr.nlamp.sfgame_backend.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.nlamp.sfgame_backend.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "items")
@Getter
@Setter
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JsonIgnore
    private Player player;

    @Column(columnDefinition = "ENUM('HELMET','WEAPON','SHIELD','ARMOR','BOOTS','GLOVES','BELT','AMULET','RING','TALISMAN','POTION','MIRROR_SHARD','DUNGEON_KEY','ALBUM')")
    @Enumerated(value = EnumType.STRING)
    private ItemType itemType;

    @Column(columnDefinition = "ENUM('EQUIPMENT','BAG','WEAPON_SHOP','MAGIC_SHOP','QUEST')")
    @Enumerated(value = EnumType.STRING)
    private SlotType slotType;

    private Boolean isLegendary;

    private Integer itemId; // image-file-name-in-directories-with-all-items

    private Integer strength;
    private Integer dexterity;
    private Integer intelligence;
    private Integer constitution;
    private Integer luck;

    private Integer armor;

    private BigInteger silverCost;
    private BigInteger mushCost;
}
