package gr.nlamp.sfgame_backend.authentication;

import gr.nlamp.sfgame_backend.appcontext.ApplicationContextProvider;
import gr.nlamp.sfgame_backend.authentication.validators.PlayerEmailExistsValidator;
import gr.nlamp.sfgame_backend.authentication.validators.PlayerUsernameExistsValidator;
import gr.nlamp.sfgame_backend.item.Item;
import gr.nlamp.sfgame_backend.item.ItemGenerator;
import gr.nlamp.sfgame_backend.item.ItemType;
import gr.nlamp.sfgame_backend.item.SlotType;
import gr.nlamp.sfgame_backend.player.Player;
import gr.nlamp.sfgame_backend.player.PlayerRepository;
import gr.nlamp.sfgame_backend.player.PlayerState;
import gr.nlamp.sfgame_backend.tavern.QuestGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PlayerRepository playerRepository;
    private final QuestGenerator questGenerator;
    private final ItemGenerator itemGenerator;

    private final List<AuthenticationValidator> validators = List.of(
            ApplicationContextProvider.getApplicationContext().getBean(PlayerUsernameExistsValidator.class),
            ApplicationContextProvider.getApplicationContext().getBean(PlayerEmailExistsValidator.class)
    );

    @Transactional(rollbackOn = Exception.class)
    public void register(final RegistrationDto registrationDto) {
        for (final AuthenticationValidator validator : validators) {
            validator.validate(registrationDto);
        }
        final Player player = new Player();
        initializePlayerBasicAttributes(player, registrationDto);
        initializePlayerSkills(player);
        initializePlayerResources(player);
        playerRepository.saveAndFlush(player);
        questGenerator.generateQuests(player, true);
        final Item initialWeapon = itemGenerator.generateItem(player, SlotType.EQUIPMENT, ItemType.WEAPON);
        player.getItems().add(initialWeapon);
        itemGenerator.generateWeaponShopItems(player, true);
        itemGenerator.generateMagicShopItems(player, true);
        playerRepository.save(player);
    }

    private void initializePlayerResources(final Player player) {
        player.setCoins(new BigInteger("10"));
        player.setMushrooms(10L);
        player.setLevel(1L);
        player.setGainedExperience(BigInteger.ZERO);
        player.setCurrentEnergy(BigDecimal.valueOf(100));
        player.setTotalUsedEnergy(BigDecimal.ZERO);
        player.setTotalBeersDrink(0);
    }

    private void initializePlayerSkills(final Player player) {
        final int[] skills = player.getRace().getSkills();
        player.setStrength(BigInteger.valueOf(skills[0]));
        player.setDexterity(BigInteger.valueOf(skills[1]));
        player.setIntelligence(BigInteger.valueOf(skills[2]));
        player.setConstitution(BigInteger.valueOf(skills[3]));
        player.setLuck(BigInteger.valueOf(skills[4]));
    }

    private void initializePlayerBasicAttributes(final Player player, final RegistrationDto registrationDto) {
        player.setUsername(registrationDto.getUsername());
        player.setPassword(registrationDto.getPassword());  // TODO Use bCrypt encryption from SecurityConfiguration
        player.setEmail(registrationDto.getEmail());
        player.setPlayerClass(registrationDto.getPlayerClass());
        player.setRace(registrationDto.getRace());
        player.setGender(registrationDto.getGender());
        player.setPlayerState(PlayerState.IDLE);
        player.setGoldenFrame(false);
        player.setActiveFor(1);
        player.setHighestActiveFor(1);
        player.setGainedExperience(BigInteger.ZERO);
        player.setBanned(false);
        player.setHonor(BigInteger.ZERO);
        player.setLastLoginDate(System.currentTimeMillis());
        player.setNumberOfSuccessQuests(0L);
        final long maxPlayers = playerRepository.count();
        final Long playerRank = maxPlayers + 1;
        player.setHighestRank(playerRank);
        player.setCurrentRank(playerRank);
    }
}
