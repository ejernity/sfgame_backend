package gr.nlamp.sfgame_backend.player;

import gr.nlamp.sfgame_backend.player.dto.BasicInfoDto;
import gr.nlamp.sfgame_backend.player.dto.EquipmentItemDtoList;
import gr.nlamp.sfgame_backend.player.dto.ProfileMainInfoDto;
import gr.nlamp.sfgame_backend.player.dto.UpdateDescriptionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    // TODO Use authentication instead of playerId on path
    @GetMapping("increase-skill/{skillType}/{playerId}")
    public ResponseEntity<Void> increaseSkill(@PathVariable("skillType") SkillType skillType,
                                              @PathVariable("playerId") long playerId) {
        playerService.increaseSkill(playerId, skillType);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("basic-info/{playerId}")
    public ResponseEntity<BasicInfoDto> getBasicInfo(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(playerService.getBasicInfo(playerId), HttpStatus.OK);
    }

    @GetMapping("profile-main-info/{playerId}")
    public ResponseEntity<ProfileMainInfoDto> getProfileMainInfo(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(playerService.getProfileMainInfo(playerId), HttpStatus.OK);
    }

    @PutMapping("description/{playerId}")
    public ResponseEntity<Void> updateDescription(@PathVariable("playerId") long playerId,
                                                  @RequestBody UpdateDescriptionDto dto) {
        playerService.updateDescription(dto, playerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("equipment/{playerId}")
    public ResponseEntity<EquipmentItemDtoList> getEquipment(@PathVariable("playerId") long playerId) {
        return new ResponseEntity<>(playerService.getEquipment(playerId), HttpStatus.OK);
    }
}
