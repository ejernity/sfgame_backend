package gr.nlamp.sfgame_backend.item;

import java.util.List;

public enum ItemType {
    HELMET,
    WEAPON,
    SHIELD,
    ARMOR,
    BOOTS,
    GLOVES,
    BELT,
    AMULET,
    RING,
    TALISMAN,
    POTION,
    MIRROR_SHARD,
    DUNGEON_KEY,
    ALBUM;

    public static final List<ItemType> weaponItemTypes = List.of(
            ItemType.WEAPON,
            ItemType.SHIELD,
            ItemType.ARMOR,
            ItemType.GLOVES,
            ItemType.BELT,
            ItemType.BOOTS,
            ItemType.HELMET
    );

    public static final List<ItemType> provideArmorItemTypes = List.of(
            ItemType.SHIELD,
            ItemType.ARMOR,
            ItemType.HELMET
    );

    public static final List<ItemType> magicItemTypes = List.of(
            ItemType.AMULET,
            ItemType.RING,
            ItemType.TALISMAN,
            ItemType.POTION
            // TODO Think of implementation for generation of dungeon keys and album item
//            ItemType.DUNGEON_KEY,
//            ItemType.ALBUM
    );
}
