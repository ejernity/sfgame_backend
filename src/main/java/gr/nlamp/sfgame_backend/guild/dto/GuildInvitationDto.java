package gr.nlamp.sfgame_backend.guild.dto;

import gr.nlamp.sfgame_backend.guild.GuildInvitationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuildInvitationDto {

    private long id;
    private String guildName;
    private GuildInvitationStatus status;

}
