package gr.nlamp.sfgame_backend.item;

import gr.nlamp.sfgame_backend.player.dto.BagItemDto;
import gr.nlamp.sfgame_backend.player.dto.BagItemDtoList;
import gr.nlamp.sfgame_backend.player.dto.EquipmentItemDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigInteger;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ItemMapper {
    public static ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    private static final BigInteger SELLING_PERCENTAGE = BigInteger.valueOf(4);

    @IterableMapping(qualifiedByName = "mapItemToEquipmentItem")
    public abstract List<EquipmentItemDto> mapItemsToEquipmentItemDtos(final List<Item> items);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "itemType", target = "itemType"),
            @Mapping(source = "itemRarity", target = "itemRarity"),
            @Mapping(source = "itemId", target = "itemId"),
            @Mapping(source = "strength", target = "strength"),
            @Mapping(source = "dexterity", target = "dexterity"),
            @Mapping(source = "intelligence", target = "intelligence"),
            @Mapping(source = "constitution", target = "constitution"),
            @Mapping(source = "luck", target = "luck"),
            @Mapping(source = "armor", target = "armor"),
            @Mapping(expression = "java(getCoinsFromSell(item))", target = "coinsFromSell"),
    })
    @Named("mapItemToEquipmentItem")
    public abstract EquipmentItemDto mapItemToEquipmentItem(final Item item);

    @IterableMapping(qualifiedByName = "mapItemToBagItem")
    public abstract List<BagItemDto> mapItemsToBagItemDtos(final List<Item> items);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "itemType", target = "itemType"),
            @Mapping(source = "slotType", target = "slotType"),
            @Mapping(source = "itemRarity", target = "itemRarity"),
            @Mapping(source = "itemId", target = "itemId"),
            @Mapping(source = "strength", target = "strength"),
            @Mapping(source = "dexterity", target = "dexterity"),
            @Mapping(source = "intelligence", target = "intelligence"),
            @Mapping(source = "constitution", target = "constitution"),
            @Mapping(source = "luck", target = "luck"),
            @Mapping(source = "armor", target = "armor"),
            @Mapping(expression = "java(getCoinsFromSell(item))", target = "coinsFromSell"),
    })
    @Named("mapItemToBagItem")
    public abstract BagItemDto mapItemToBagItem(final Item item);

    @Named("getCoinsFromSell")
    public BigInteger getCoinsFromSell(final Item item) {
        return item.getCoinCost().divide(SELLING_PERCENTAGE);
    }
}
