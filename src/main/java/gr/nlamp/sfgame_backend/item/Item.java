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

    @Column(columnDefinition = "ENUM('EQUIPMENT','BAG_1','BAG_2','BAG_3','BAG_4','BAG_5','BAG_6','BAG_7','BAG_8','BAG_9','BAG_10','BAG_11','BAG_12','BAG_13','BAG_14','BAG_15','BAG_16','WEAPON_SHOP_1','WEAPON_SHOP_2','WEAPON_SHOP_3','WEAPON_SHOP_4','WEAPON_SHOP_5','WEAPON_SHOP_6','MAGIC_SHOP_1','MAGIC_SHOP_2','MAGIC_SHOP_3','MAGIC_SHOP_4','MAGIC_SHOP_5','MAGIC_SHOP_6','QUEST')")
    @Enumerated(value = EnumType.STRING)
    private SlotType slotType;

    @Column(columnDefinition = "ENUM('COMMON','EPIC','LEGENDARY')")
    @Enumerated(value = EnumType.STRING)
    private ItemRarity itemRarity;

    private Integer itemId; // image-file-name-in-directories-with-all-items

    private Integer strength = 0;
    private Integer dexterity = 0;
    private Integer intelligence = 0;
    private Integer constitution = 0;
    private Integer luck = 0;

    private Integer armor = 0;

    private BigInteger coinCost;
    private BigInteger mushCost;
}
