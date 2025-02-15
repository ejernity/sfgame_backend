package gr.nlamp.sfgame_backend.guild.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@AllArgsConstructor
public class GuildMessagesDto {

    private Set<GuildMessageDto> messageDtoList = new TreeSet<>();

}
