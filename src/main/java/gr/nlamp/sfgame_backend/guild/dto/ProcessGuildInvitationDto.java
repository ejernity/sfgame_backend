package gr.nlamp.sfgame_backend.guild.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessGuildInvitationDto {

    @NotNull
    private long guildId;
}
