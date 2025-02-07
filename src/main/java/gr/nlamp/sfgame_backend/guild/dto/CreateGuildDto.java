package gr.nlamp.sfgame_backend.guild.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateGuildDto {

    @NotBlank
    private String name;

}
