package gr.nlamp.sfgame_backend.guild;

import gr.nlamp.sfgame_backend.guild.dto.*;
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

    @GetMapping("invite/{playerId}")
    public ResponseEntity<GuildInvitationsDto> getInvitations(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(guildService.getInvitations(playerId), HttpStatus.OK);
    }

    @PostMapping("invite/{playerId}")
    public ResponseEntity<Void> invite(@PathVariable("playerId") long playerId,
                                       @RequestBody @Valid CreateGuildInvitationDto dto) {
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

    @PostMapping("donate/gold/{playerId}")
    public ResponseEntity<Void> donateGold(@PathVariable("playerId") long playerId,
                                           @RequestBody DonateQuantityDto dto) {
        guildService.donateGold(dto, playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("donate/mushrooms/{playerId}")
    public ResponseEntity<Void> donateMushrooms(@PathVariable("playerId") long playerId,
                                                @RequestBody DonateQuantityDto dto) {
        guildService.donateMushrooms(dto, playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("messages/{playerId}")
    public ResponseEntity<GuildMessagesDto> getMessages(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(guildService.getMessages(playerId), HttpStatus.OK);
    }

    @PostMapping("messages/{playerId}")
    public ResponseEntity<Void> sendMessage(@PathVariable("playerId") long playerId,
                                            @RequestBody @Valid PostGuildMessageDto dto) {
        guildService.sendMessage(dto, playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("description/{playerId}")
    public ResponseEntity<Void> updateDescription(@PathVariable("playerId") long playerId,
                                            @RequestBody @Valid UpdateGuildDescriptionDto dto) {
        guildService.updateDescription(dto, playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{playerId}")
    public ResponseEntity<GuildDto> getGuild(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(guildService.getGuild(playerId), HttpStatus.OK);
    }

    @PutMapping("rank/{playerId}")
    public ResponseEntity<Void> updateRank(@PathVariable("playerId") long playerId,
                                           @RequestBody ChangeRankDto dto) {
        guildService.updateRank(dto, playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("kick-off/{memberId}/{playerId}")
    public ResponseEntity<Void> kickOff(@PathVariable("memberId") long memberId,
                                        @PathVariable("playerId") long playerId) {
        guildService.kickOff(memberId, playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
