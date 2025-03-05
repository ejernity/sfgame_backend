package gr.nlamp.sfgame_backend.player.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class SkillsAnalyticalDto {

    // base skills
    private BigInteger baseStrength;
    private BigInteger baseDexterity;
    private BigInteger baseIntelligence;
    private BigInteger baseConstitution;
    private BigInteger baseLuck;

    // equipment bonus
    private BigInteger equipmentStrength;
    private BigInteger equipmentDexterity;
    private BigInteger equipmentIntelligence;
    private BigInteger equipmentConstitution;
    private BigInteger equipmentLuck;

    // booster bonus
    private BigInteger boosterStrength;
    private BigInteger boosterDexterity;
    private BigInteger boosterIntelligence;
    private BigInteger boosterConstitution;
    private BigInteger boosterLuck;

    // armor
    private Integer totalArmor;

    // hit points
    private BigInteger hitPoints;

    public SkillsAnalyticalDto() {
        equipmentStrength = BigInteger.ZERO;
        equipmentDexterity = BigInteger.ZERO;
        equipmentIntelligence = BigInteger.ZERO;
        equipmentConstitution = BigInteger.ZERO;
        equipmentLuck = BigInteger.ZERO;
        boosterStrength = BigInteger.ZERO;
        boosterDexterity = BigInteger.ZERO;
        boosterIntelligence = BigInteger.ZERO;
        boosterConstitution = BigInteger.ZERO;
        boosterLuck = BigInteger.ZERO;
        hitPoints = BigInteger.ZERO;
    }

}
