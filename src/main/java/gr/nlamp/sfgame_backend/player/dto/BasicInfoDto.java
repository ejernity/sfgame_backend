package gr.nlamp.sfgame_backend.player.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class BasicInfoDto {

    private long level;
    private BigInteger coins;
    private long mushrooms;

}
