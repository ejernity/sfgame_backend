package gr.nlamp.sfgame_backend.player.dto;

import gr.nlamp.sfgame_backend.stable.Mount;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MountDto {

    private Mount mount;
    private LocalDateTime activeUntil;
    
}
