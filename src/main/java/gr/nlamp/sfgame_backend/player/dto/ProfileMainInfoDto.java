package gr.nlamp.sfgame_backend.player.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
public class ProfileMainInfoDto {

    private String username;
    private String description;
    private long level;
    private BigInteger gainedExperience;
    private BigInteger experienceForNextLevel;
    private BigInteger honor;
    private long numberOfSuccessQuests;

}
