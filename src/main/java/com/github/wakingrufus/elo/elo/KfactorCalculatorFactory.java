package com.github.wakingrufus.elo.elo;

import org.jvnet.hk2.annotations.Service;

@Service
public class KfactorCalculatorFactory {
    private KfactorCalculator calculator;

    public KfactorCalculator build() {
        if (calculator == null) {
            calculator = new KfactorCalculator();
        }
        return calculator;
    }
}
