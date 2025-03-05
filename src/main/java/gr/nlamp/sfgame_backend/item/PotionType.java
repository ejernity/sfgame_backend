package gr.nlamp.sfgame_backend.item;

import lombok.Getter;

import java.util.List;

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

    private final double percentage;
    private final Integer days;

    PotionType(double percentage, int days) {
        this.percentage = percentage;
        this.days = days;
    }

    public static final List<PotionType> STRENGTH_POTIONS = List.of(STRENGTH_SMALL, STRENGTH_MEDIUM, STRENGTH_LARGE);
    public static final List<PotionType> DEXTERITY_POTIONS = List.of(DEXTERITY_SMALL, DEXTERITY_MEDIUM, DEXTERITY_LARGE);
    public static final List<PotionType> INTELLIGENCE_POTIONS = List.of(INTELLIGENCE_SMALL, INTELLIGENCE_MEDIUM, INTELLIGENCE_LARGE);
    public static final List<PotionType> CONSTITUTION_POTIONS = List.of(CONSTITUTION_SMALL, CONSTITUTION_MEDIUM, CONSTITUTION_LARGE);
    public static final List<PotionType> LUCK_POTIONS = List.of(LUCK_SMALL, LUCK_MEDIUM, LUCK_LARGE);
}
