package gr.nlamp.sfgame_backend.stable;

import lombok.Getter;

@Getter
public enum Mount {
    PIG(0.05, 5, 0, 1),
    DONKEY(0.10, 7, 1, 10),
    TIGER(0.25, 14, 5, 25),
    DRAGON(0.50, 14, 25, 0);

    private double percentageBooster;
    private int duration;
    private int mushCost;
    private int coinCost;

    Mount(double percentageBooster, int duration, int mushCost, int coinCost) {
        this.percentageBooster = percentageBooster;
        this.duration = duration;
        this.mushCost = mushCost;
        this.coinCost = coinCost;
    }
}
