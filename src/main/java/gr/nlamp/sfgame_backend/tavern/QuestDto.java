package gr.nlamp.sfgame_backend.tavern;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
public class QuestDto {
    private short orderNo;
    private int duration;
    private BigInteger coins;
    private BigInteger experience;
    private Boolean hasItemReward;
    private long mushrooms;
    private Boolean isChosen;
    private long chosenAt;
    // TODO Fetch item when quests are made with item reward
    //    private ItemDto item;
}
