package gr.nlamp.sfgame_backend.guild;

import gr.nlamp.sfgame_backend.guild.dto.GuildMemberDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.TreeSet;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class GuildMemberMapper {
    public static GuildMemberMapper INSTANCE = Mappers.getMapper(GuildMemberMapper.class);

    @IterableMapping(qualifiedByName = "toDto")
    public abstract TreeSet<GuildMemberDto> mapMembers(Set<GuildMember> guildMembers);

    @Mappings({
            @Mapping(source = "player.id", target = "playerId"),
            @Mapping(source = "player.username", target = "username"),
            @Mapping(source = "playerRank", target = "rank"),
            @Mapping(source = "goldDonated", target = "goldDonated"),
            @Mapping(source = "mushroomDonated", target = "mushroomDonated"),
    })
    @Named("toDto")
    public abstract GuildMemberDto toDto(GuildMember guildMember);
}
