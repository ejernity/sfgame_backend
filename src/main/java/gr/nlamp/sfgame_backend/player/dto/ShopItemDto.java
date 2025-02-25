package gr.nlamp.sfgame_backend.player.dto;

import gr.nlamp.sfgame_backend.item.SlotType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopItemDto extends BagItemDto {

    private long mushCost;

}
