package gr.nlamp.sfgame_backend.tavern;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class QuestMapper {
    public static QuestMapper INSTANCE = Mappers.getMapper(QuestMapper.class);

    public abstract List<QuestDto> mapList(List<Quest> questList);

    @Mappings({
            @Mapping(source = "orderNo", target = "orderNo"),
            @Mapping(source = "duration", target = "duration"),
            @Mapping(source = "coins", target = "coins"),
            @Mapping(source = "experience", target = "experience"),
            @Mapping(source = "mushrooms", target = "mushrooms"),
            @Mapping(source = "hasItemReward", target = "hasItemReward"),
            @Mapping(source = "isChosen", target = "isChosen"),
            @Mapping(source = "chosenAt", target = "chosenAt"),
    })
    public abstract QuestDto toDto(final Quest quest);

    @Mappings({
            @Mapping(source = "coins", target = "coins"),
            @Mapping(source = "experience", target = "experience"),
            @Mapping(source = "hasItemReward", target = "hasItemReward"),
            @Mapping(source = "mushrooms", target = "mushrooms"),
    })
    public abstract RewardDto toRewardDto(final Quest quest);
}
