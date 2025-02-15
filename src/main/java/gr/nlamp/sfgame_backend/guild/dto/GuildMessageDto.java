package gr.nlamp.sfgame_backend.guild.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuildMessageDto implements Comparable<GuildMessageDto> {

    private String sender;
    private String message;
    private Long timeStamp;

    @Override
    public int compareTo(GuildMessageDto o) {
        return this.timeStamp.compareTo(o.timeStamp);
    }
}
