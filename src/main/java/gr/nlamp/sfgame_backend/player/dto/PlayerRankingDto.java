package gr.nlamp.sfgame_backend.player.dto;

import gr.nlamp.sfgame_backend.player.Class;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class PlayerRankingDto {

    private long id;
    private String username;
    private BigInteger honor;
    private Long currentRank;
    private Class playerClass;
    private Long level;

}
