package gr.nlamp.sfgame_backend.item.booster;

import gr.nlamp.sfgame_backend.item.Item;
import gr.nlamp.sfgame_backend.player.dto.BoosterDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class BoosterMapper {
    public static BoosterMapper INSTANCE = Mappers.getMapper(BoosterMapper.class);

    private static final BigInteger SELLING_PERCENTAGE = BigInteger.valueOf(4);

    @IterableMapping(qualifiedByName = "mapBoosterToBoosterDto")
    public abstract List<BoosterDto> mapBoostersToBoosterDtos(final List<Booster> boosters);

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
            @Mapping(expression = "java(getCoinsFromSell(booster))", target = "coinCost"),
            @Mapping(source = "potionType", target = "potionType"),
            @Mapping(expression = "java(mapActiveUntilMillisToLocalDateTime(booster))", target = "activeUntil"),
    })
    @Named("mapBoosterToBoosterDto")
    public abstract BoosterDto mapBoosterToBoosterDto(final Booster booster);

    @Named("getCoinsFromSell")
    public BigInteger getCoinsFromSell(final Booster booster) {
        return booster.getCoinCost().divide(SELLING_PERCENTAGE);
    }

    @Named("mapActiveUntilMillisToLocalDateTime")
    public LocalDateTime mapActiveUntilMillisToLocalDateTime(final Booster booster) {
        return Instant.ofEpochMilli(booster.getActiveUntil()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
