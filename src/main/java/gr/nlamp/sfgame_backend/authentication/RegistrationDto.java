package gr.nlamp.sfgame_backend.authentication;

import gr.nlamp.sfgame_backend.player.Class;
import gr.nlamp.sfgame_backend.player.Gender;
import gr.nlamp.sfgame_backend.player.Race;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private Class playerClass;

    @NotNull
    private Race race;

    @NotNull
    private Gender gender;

}
