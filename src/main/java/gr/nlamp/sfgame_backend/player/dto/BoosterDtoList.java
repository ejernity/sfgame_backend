package gr.nlamp.sfgame_backend.player.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BoosterDtoList {

    private List<BoosterDto> boosters;

}
