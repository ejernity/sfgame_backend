package gr.nlamp.sfgame_backend.guild.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GuildInvitationsDto {

    private List<GuildInvitationDto> invitations;

}
