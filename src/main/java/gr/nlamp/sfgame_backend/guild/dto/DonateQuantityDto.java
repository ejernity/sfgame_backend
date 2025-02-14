package gr.nlamp.sfgame_backend.guild.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class DonateQuantityDto {

    private BigInteger gold;
    private long mushrooms;

}
