package gr.nlamp.sfgame_backend.stable;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("mount")
public class MountController {

    private final MountService mountService;

    @GetMapping("{playerId}/{mount}")
    public ResponseEntity<Void> buyMount(@PathVariable("playerId") final long playerId,
                                      @PathVariable("mount") final Mount mount) {
        mountService.buyMount(playerId, mount);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
