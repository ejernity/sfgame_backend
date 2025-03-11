package gr.nlamp.sfgame_backend.player.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

@Getter
@Setter
@AllArgsConstructor
public class PlayersRankingDto {

    private Slice<PlayerRankingDto> players;

}
