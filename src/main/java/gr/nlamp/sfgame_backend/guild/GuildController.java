package gr.nlamp.sfgame_backend.guild;

import gr.nlamp.sfgame_backend.guild.dto.CreateGuildDto;
import gr.nlamp.sfgame_backend.guild.dto.GuildInvitationDto;
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
    
}
