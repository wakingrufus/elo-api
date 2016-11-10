package com.github.wakingrufus.elo.elo;

import java.math.BigDecimal;

public class AdjustmentCalculator {

    public int calculate(int kFactor, BigDecimal score, BigDecimal expectedScore) {
        return (new BigDecimal(kFactor).multiply(score.subtract(expectedScore))).intValue();
    }
}
