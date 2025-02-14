package gr.nlamp.sfgame_backend.guild;

import gr.nlamp.sfgame_backend.guild.dto.CreateGuildDto;
import gr.nlamp.sfgame_backend.guild.dto.GuildInvitationDto;
import gr.nlamp.sfgame_backend.guild.dto.ProcessGuildInvitationDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("guild")
public class GuildController {

    private final GuildService guildService;

    // TODO Change path variable to Authentication when you add security
    @PostMapping("create/{playerId}")
    public ResponseEntity<Void> create(@PathVariable("playerId") long playerId,
                                    @RequestBody @Valid CreateGuildDto dto) {
        guildService.create(dto, playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("invite/{playerId}")
    public ResponseEntity<Void> invite(@PathVariable("playerId") long playerId,
                                       @RequestBody @Valid GuildInvitationDto dto) {
        guildService.invite(dto, playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("invite/accept/{playerId}")
    public ResponseEntity<Void> acceptInvitation(@PathVariable("playerId") long playerId,
                                                 @RequestBody @Valid ProcessGuildInvitationDto dto) {
        guildService.acceptInvitation(dto, playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("invite/reject/{playerId}")
    public ResponseEntity<Void> rejectInvitation(@PathVariable("playerId") long playerId,
                                                 @RequestBody @Valid ProcessGuildInvitationDto dto) {
        guildService.rejectInvitation(dto, playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("upgrade/treasure/{playerId}")
    public ResponseEntity<Void> upgradeTreasure(@PathVariable("playerId") long playerId) {
        guildService.upgradeTreasure(playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("upgrade/instructor/{playerId}")
    public ResponseEntity<Void> upgradeInstructor(@PathVariable("playerId") long playerId) {
        guildService.upgradeInstructor(playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
