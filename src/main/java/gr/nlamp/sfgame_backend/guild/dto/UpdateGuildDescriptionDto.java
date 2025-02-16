package gr.nlamp.sfgame_backend.guild.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGuildDescriptionDto {

    @NotNull
    private String description;

}
