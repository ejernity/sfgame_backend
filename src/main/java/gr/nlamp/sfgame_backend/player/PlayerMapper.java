package gr.nlamp.sfgame_backend.player;

import gr.nlamp.sfgame_backend.player.dto.PlayerRankingDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Slice;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PlayerMapper {
    public static PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    @IterableMapping(qualifiedByName = "mapPlayerToPlayerRankingDto")
    public abstract List<PlayerRankingDto> mapPlayerListToPlayerRankingDtoList(Slice<Player> players);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "honor", target = "honor"),
            @Mapping(source = "currentRank", target = "currentRank"),
            @Mapping(source = "playerClass", target = "playerClass"),
            @Mapping(source = "level", target = "level"),
    })
    @Named("mapPlayerToPlayerRankingDto")
    public abstract PlayerRankingDto mapPlayerToPlayerRankingDto(Player player);
}
