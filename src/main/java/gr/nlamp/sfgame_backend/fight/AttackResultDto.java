package gr.nlamp.sfgame_backend.fight;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class AttackResultDto {
    private long attackerId;
    private long defenderId;
    private int damage;
    private boolean dodged;
    private boolean blocked;
    private boolean critical;
    private BigInteger defenderRemainingHp;
}

