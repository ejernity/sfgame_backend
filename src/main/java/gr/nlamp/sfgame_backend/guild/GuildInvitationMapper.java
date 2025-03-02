package gr.nlamp.sfgame_backend.guild;

import gr.nlamp.sfgame_backend.guild.dto.GuildInvitationDto;
import gr.nlamp.sfgame_backend.guild.dto.GuildMessageDto;
import gr.nlamp.sfgame_backend.player.Player;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class GuildInvitationMapper {
    public static GuildInvitationMapper INSTANCE = Mappers.getMapper(GuildInvitationMapper.class);

    public abstract List<GuildInvitationDto> mapList(List<GuildInvitation> guildInvitationList);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "guild.name", target = "guildName"),
            @Mapping(source = "status", target = "status")
    })
    public abstract GuildInvitationDto map(GuildInvitation guildInvitation);

//    @Named("getGuildName")
//    public String getSender(GuildMessage guildMessage) {
//        final Player player = guildMessage.getPlayer();
//        return player != null ? player.getUsername() : null;
//    }
}
