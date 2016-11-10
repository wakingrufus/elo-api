package com.github.wakingrufus.elo.elo;

import org.jvnet.hk2.annotations.Service;

@Service
public class AdjustmentCalculatorFactory {
    private AdjustmentCalculator adjustmentCalculator;

    public AdjustmentCalculator build() {
        if (adjustmentCalculator == null) {
            adjustmentCalculator = new AdjustmentCalculator();
        }
        return adjustmentCalculator;
    }
}
