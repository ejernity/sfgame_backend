package gr.nlamp.sfgame_backend.player;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
