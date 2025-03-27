package gr.nlamp.sfgame_backend.player.dto;

import gr.nlamp.sfgame_backend.item.ItemRarity;
import gr.nlamp.sfgame_backend.item.ItemType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class EquipmentItemDto {

    private long id;
    private ItemType itemType;
    private ItemRarity itemRarity;
    private String itemId;
    private int strength;
    private int dexterity;
    private int intelligence;
    private int constitution;
    private int luck;
    private int armor;
    private BigInteger coinCost;

}
