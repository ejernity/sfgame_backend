package gr.nlamp.sfgame_backend.authentication.validators;

import gr.nlamp.sfgame_backend.authentication.AuthenticationValidator;
import gr.nlamp.sfgame_backend.authentication.RegistrationDto;
import gr.nlamp.sfgame_backend.player.Player;
import gr.nlamp.sfgame_backend.player.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerEmailExistsValidator implements AuthenticationValidator {

    private final PlayerRepository playerRepository;

    @Override
    public void validate(final RegistrationDto registrationDto) {
        final Player player = playerRepository.findByEmail(registrationDto.getEmail());
        if (player != null)
            throw new RuntimeException("There is already a player with that email");
    }
}
