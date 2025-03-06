package gr.nlamp.sfgame_backend.tavern.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuestsDto {
    private List<QuestDto> quests;
}
