package gr.nlamp.sfgame_backend.stable;

import lombok.Getter;

@Getter
public enum Mount {
    PIG(0.05, 5, 0, 1),
    DONKEY(0.10, 7, 1, 10),
    TIGER(0.25, 14, 5, 25),
    DRAGON(0.50, 14, 25, 0);

    private final double percentageBooster;
    private final int duration;
    private final int mushCost;
    private final int coinCost;

    Mount(final double percentageBooster, final int duration, final int mushCost, final int coinCost) {
        this.percentageBooster = percentageBooster;
        this.duration = duration;
        this.mushCost = mushCost;
        this.coinCost = coinCost;
    }
}
