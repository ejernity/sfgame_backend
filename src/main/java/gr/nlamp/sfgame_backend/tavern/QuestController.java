package gr.nlamp.sfgame_backend.tavern;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("quest")
public class QuestController {

    private final QuestService questService;

    // TODO Change path variable to Authentication when you add security
    @GetMapping("{playerId}")
    public ResponseEntity<QuestsDto> getAll(@PathVariable("playerId") final long playerId) {
        return new ResponseEntity<>(questService.getAll(playerId), HttpStatus.OK);
    }

    @PostMapping("{playerId}/{orderNo}")
    public ResponseEntity<Void> start(@PathVariable("playerId") final long playerId,
                                      @PathVariable("orderNo") final short orderNo) {
        questService.start(playerId, orderNo);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{playerId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable("playerId") final long playerId) {
        questService.cancel(playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{playerId}/finish")
    public ResponseEntity<RewardDto> finish(@PathVariable("playerId") final long playerId) {
        return new ResponseEntity<>(questService.finish(playerId), HttpStatus.OK);
    }

}