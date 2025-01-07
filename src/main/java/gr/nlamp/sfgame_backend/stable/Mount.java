package gr.nlamp.sfgame_backend.stable;

import lombok.Getter;

@Getter
public enum Mount {
    PIG(0.95, 5, 0, 1),
    DONKEY(0.90, 7, 1, 10),
    TIGER(0.75, 14, 5, 25),
    DRAGON(0.50, 14, 25, 0);

    private final double percentageBooster;
    private final long duration;
    private final int mushCost;
    private final long coinCost;

    Mount(final double percentageBooster, final long duration, final int mushCost, final long coinCost) {
        this.percentageBooster = percentageBooster;
        this.duration = duration;
        this.mushCost = mushCost;
        this.coinCost = coinCost;
    }
}
