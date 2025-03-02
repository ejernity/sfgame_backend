package gr.nlamp.sfgame_backend.guild.dto;

import gr.nlamp.sfgame_backend.guild.Rank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRankDto {

    private long playerId;
    private Rank newRank;

}
