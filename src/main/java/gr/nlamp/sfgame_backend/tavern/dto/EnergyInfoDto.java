package gr.nlamp.sfgame_backend.tavern.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EnergyInfoDto {

    private BigDecimal currentEnergy;
    private BigDecimal totalUsedEnergy;
    private Integer totalBeersDrink;

}
