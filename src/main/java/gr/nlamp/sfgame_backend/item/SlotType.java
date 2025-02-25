package gr.nlamp.sfgame_backend.item;

import java.util.List;

public enum SlotType {
    EQUIPMENT,
    BAG_1,
    BAG_2,
    BAG_3,
    BAG_4,
    BAG_5,
    BAG_6,
    BAG_7,
    BAG_8,
    BAG_9,
    BAG_10,
    BAG_11,
    BAG_12,
    BAG_13,
    BAG_14,
    BAG_15,
    BAG_16,
    WEAPON_SHOP_1,
    WEAPON_SHOP_2,
    WEAPON_SHOP_3,
    WEAPON_SHOP_4,
    WEAPON_SHOP_5,
    WEAPON_SHOP_6,
    MAGIC_SHOP_1,
    MAGIC_SHOP_2,
    MAGIC_SHOP_3,
    MAGIC_SHOP_4,
    MAGIC_SHOP_5,
    MAGIC_SHOP_6,
    QUEST;

    public static final List<SlotType> magicShopSlots = List.of(
            MAGIC_SHOP_1, MAGIC_SHOP_2,
            MAGIC_SHOP_3, MAGIC_SHOP_4,
            MAGIC_SHOP_5, MAGIC_SHOP_6
    );

    public static final List<SlotType> weaponShopSlots = List.of(
            WEAPON_SHOP_1, WEAPON_SHOP_2,
            WEAPON_SHOP_3, WEAPON_SHOP_4,
            WEAPON_SHOP_5, WEAPON_SHOP_6
    );

    public static final List<SlotType> equipmentSlots = List.of(EQUIPMENT);
    
    public static final List<SlotType> bagSlots = List.of(
            BAG_1,
            BAG_2,
            BAG_3,
            BAG_4,
            BAG_5,
            BAG_6,
            BAG_7,
            BAG_8,
            BAG_9,
            BAG_10,
            BAG_11,
            BAG_12,
            BAG_13,
            BAG_14,
            BAG_15,
            BAG_16
    );
}
