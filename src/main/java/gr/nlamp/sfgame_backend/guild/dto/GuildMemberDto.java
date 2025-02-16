package gr.nlamp.sfgame_backend.guild.dto;

import gr.nlamp.sfgame_backend.guild.Rank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class GuildMemberDto implements Comparable<GuildMemberDto> {

    private long playerId;
    private String username;
    private Rank rank;
    private BigInteger goldDonated;
    private long mushroomDonated;

    @Override
    public int compareTo(GuildMemberDto o) {
        final int rankDiff = this.rank.ordinal() - o.rank.ordinal();
        if (rankDiff == 0)
            return this.username.compareTo(o.username);
        return rankDiff;
    }
}
