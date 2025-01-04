package gr.nlamp.sfgame_backend.authentication;

@FunctionalInterface
public interface AuthenticationValidator {

    void validate(final RegistrationDto registrationDto);
}
