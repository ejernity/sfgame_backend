package gr.nlamp.sfgame_backend.player.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BagItemDtoList {

    private List<BagItemDto> items;
}
