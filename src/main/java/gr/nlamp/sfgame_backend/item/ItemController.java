package gr.nlamp.sfgame_backend.item;

import gr.nlamp.sfgame_backend.item.dto.MoveItemRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("item")
public class ItemController {

    private final ItemService itemService;

    @PostMapping("{playerId}/move-bag-item")
    public ResponseEntity<Void> moveItem(@PathVariable("playerId") final long playerId,
                                         @Valid @RequestBody final MoveItemRequestDto dto) {
        itemService.moveBagItem(playerId, dto);
        return ResponseEntity.noContent().build();
    }
}
