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

    @GetMapping("{playerId}/equip/{itemId}")
    public ResponseEntity<Void> equip(@PathVariable("playerId") final long playerId,
                                      @PathVariable("itemId") final long itemId) {
        itemService.equip(playerId, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{playerId}/unequip/{itemId}")
    public ResponseEntity<Void> unequip(@PathVariable("playerId") final long playerId,
                                      @PathVariable("itemId") final long itemId) {
        itemService.unequip(playerId, itemId);
        return ResponseEntity.noContent().build();
    }
}
