package gr.nlamp.sfgame_backend.tavern;

import gr.nlamp.sfgame_backend.player.Player;
import gr.nlamp.sfgame_backend.tavern.dto.QuestDto;
import gr.nlamp.sfgame_backend.tavern.dto.RewardDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class QuestMapper {
    public static QuestMapper INSTANCE = Mappers.getMapper(QuestMapper.class);

    public abstract List<QuestDto> mapList(List<Quest> questList);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "orderNo", target = "orderNo"),
            @Mapping(expression = "java(calculateDuration(quest))", target = "duration"),
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

    @Named("calculateDuration")
    public BigDecimal calculateDuration(final Quest quest) {
        final Player player = quest.getPlayer();
        if (player.getMount() != null) {
            final BigDecimal booster = BigDecimal.valueOf(player.getMount().getPercentageBooster());
            return quest.getDuration().multiply(booster);
        }
        return quest.getDuration();
    }
}
