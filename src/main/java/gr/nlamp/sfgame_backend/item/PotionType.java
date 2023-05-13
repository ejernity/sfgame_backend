package gr.nlamp.sfgame_backend.item;

import lombok.Getter;

@Getter
public enum PotionType {
    STRENGTH_SMALL(0.10, 3),
    STRENGTH_MEDIUM(0.15, 3),
    STRENGTH_LARGE(0.25, 3),
    DEXTERITY_SMALL(0.10, 3),
    DEXTERITY_MEDIUM(0.15, 3),
    DEXTERITY_LARGE(0.25, 3),
    INTELLIGENCE_SMALL(0.10, 3),
    INTELLIGENCE_MEDIUM(0.15, 3),
    INTELLIGENCE_LARGE(0.25, 3),
    CONSTITUTION_SMALL(0.10, 3),
    CONSTITUTION_MEDIUM(0.15, 3),
    CONSTITUTION_LARGE(0.25, 3),
    LUCK_SMALL(0.10, 3),
    LUCK_MEDIUM(0.15, 3),
    LUCK_LARGE(0.25, 3),
    ETERNAL_LIFE(0.25, 7);

    private Double percentage;
    private Integer days;

    PotionType(double percentage, int days) {
        this.percentage = percentage;
        this.days = days;
    }
}
