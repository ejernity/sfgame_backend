package gr.nlamp.sfgame_backend.guild;

import gr.nlamp.sfgame_backend.guild.dto.GuildMessageDto;
import gr.nlamp.sfgame_backend.player.Player;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.TreeSet;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class GuildMessageMapper {
    public static GuildMessageMapper INSTANCE = Mappers.getMapper(GuildMessageMapper.class);

    public abstract TreeSet<GuildMessageDto> mapSet(Set<GuildMessage> guildMessageSet);

    @Mappings({
            @Mapping(source = "message", target = "message"),
            @Mapping(expression = "java(getSender(guildMessage))", target = "sender"),
            @Mapping(source = "timeStamp", target = "timeStamp")
    })
    public abstract GuildMessageDto map(GuildMessage guildMessage);

    @Named("getSender")
    public String getSender(GuildMessage guildMessage) {
        final Player player = guildMessage.getPlayer();
        return player != null ? player.getUsername() : null;
    }
}
