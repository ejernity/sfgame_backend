package gr.nlamp.sfgame_backend.item.dto;

import gr.nlamp.sfgame_backend.item.SlotType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MoveItemRequestDto {

    @NotNull
    private long itemId;

    @NotNull
    private SlotType destinationSlot;
}
