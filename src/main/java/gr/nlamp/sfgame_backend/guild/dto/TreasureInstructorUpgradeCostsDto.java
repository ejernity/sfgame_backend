package gr.nlamp.sfgame_backend.guild.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class TreasureInstructorUpgradeCostsDto {
    private BigInteger gold;
    private long mushrooms;
}
