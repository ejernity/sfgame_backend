package gr.nlamp.sfgame_backend.guild.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class GuildDto {

    private long id;
    private String name;
    private String description;
    private BigInteger honor;
    private BigInteger gold;
    private long mushrooms;
    private int treasureLevel;
    private int instructorLevel;
    private Set<GuildMemberDto> members;
}
