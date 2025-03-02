package gr.nlamp.sfgame_backend.guild.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateGuildInvitationDto {

    @NotNull
    private long playerId;
}
