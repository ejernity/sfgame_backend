package gr.nlamp.sfgame_backend.player.dto;

import gr.nlamp.sfgame_backend.item.PotionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoosterDto extends EquipmentItemDto {

    private PotionType potionType;
    private LocalDateTime activeUntil;

}
