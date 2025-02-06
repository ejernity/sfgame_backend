package gr.nlamp.sfgame_backend.item.booster;

import gr.nlamp.sfgame_backend.item.Item;
import gr.nlamp.sfgame_backend.item.PotionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "boosters")
@Getter
@Setter
public class Booster extends Item {

    @Column(columnDefinition = "ENUM('STRENGTH_SMALL','STRENGTH_MEDIUM','STRENGTH_LARGE'," +
            "'DEXTERITY_SMALL','DEXTERITY_MEDIUM','DEXTERITY_LARGE'," +
            "'INTELLIGENCE_SMALL','INTELLIGENCE_MEDIUM','INTELLIGENCE_LARGE'," +
            "'CONSTITUTION_SMALL','CONSTITUTION_MEDIUM','CONSTITUTION_LARGE'," +
            "'LUCK_SMALL','LUCK_MEDIUM','LUCK_LARGE','ETERNAL_LIFE')")
    @Enumerated(value = EnumType.STRING)
    private PotionType potionType;
    private Long activeUntil;
}
