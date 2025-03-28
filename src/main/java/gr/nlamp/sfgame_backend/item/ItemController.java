package gr.nlamp.sfgame_backend.item;

import gr.nlamp.sfgame_backend.item.dto.MoveItemRequestDto;
import gr.nlamp.sfgame_backend.player.dto.BagItemDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<BagItemDto> unequip(@PathVariable("playerId") final long playerId,
                                              @PathVariable("itemId") final long itemId) {
        return new ResponseEntity<>(itemService.unequip(playerId, itemId), HttpStatus.OK);
    }

    @GetMapping("{playerId}/buy/{itemId}")
    public ResponseEntity<Void> buy(@PathVariable("playerId") final long playerId,
                                    @PathVariable("itemId") final long itemId) {
        itemService.buy(playerId, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{playerId}/sell/{itemId}")
    public ResponseEntity<Void> sell(@PathVariable("playerId") final long playerId,
                                    @PathVariable("itemId") final long itemId) {
        itemService.sell(playerId, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{playerId}/consume-potion/{itemId}")
    public ResponseEntity<Void> consumePotion(@PathVariable("playerId") final long playerId,
                                     @PathVariable("itemId") final long boosterId) {
        itemService.consumePotion(playerId, boosterId);
        return ResponseEntity.noContent().build();
    }
}
