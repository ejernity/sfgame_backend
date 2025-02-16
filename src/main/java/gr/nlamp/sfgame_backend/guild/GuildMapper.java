package gr.nlamp.sfgame_backend.guild;

import gr.nlamp.sfgame_backend.guild.dto.GuildDto;
import gr.nlamp.sfgame_backend.guild.dto.GuildMemberDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class GuildMapper {
    public static GuildMapper INSTANCE = Mappers.getMapper(GuildMapper.class);

    private final GuildMemberMapper guildMemberMapper = GuildMemberMapper.INSTANCE;

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "honor", target = "honor"),
            @Mapping(source = "gold", target = "gold"),
            @Mapping(source = "mushrooms", target = "mushrooms"),
            @Mapping(source = "treasureLevel", target = "treasureLevel"),
            @Mapping(source = "instructorLevel", target = "instructorLevel"),
            @Mapping(expression = "java(mapMembers(guild))", target = "members"),
    })
    public abstract GuildDto toDto(Guild guild);

    @Named("mapMembers")
    public Set<GuildMemberDto> mapMembers(Guild guild) {
        return guildMemberMapper.mapMembers(guild.getGuildMembers());
    }
}
