package gr.nlamp.sfgame_backend.guild.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostGuildMessageDto {

    @NotBlank
    private String message;

}
