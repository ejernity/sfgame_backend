package gr.nlamp.sfgame_backend.fight;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("fight")
@RequiredArgsConstructor
public class FightController {
    private final FightService fightService;

    @GetMapping("pvp/{attackerId}/{defenderId}")
    public ResponseEntity<List<AttackResultDto>> simulatePvp(
            @PathVariable("attackerId") long attackerId,
            @PathVariable("defenderId") long defenderId) {
        final List<AttackResultDto> result = fightService.simulatePvpFight(attackerId, defenderId);

        return ResponseEntity.ok(result);
    }
}
