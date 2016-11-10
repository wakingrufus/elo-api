package com.github.wakingrufus.elo.elo;

import org.jvnet.hk2.annotations.Service;

@Service
public class ExpectedScoreCalculatorFactory {
    public ExpectedScoreCalculator build(int xi) {
        ExpectedScoreCalculator expectedScoreCalculator = new ExpectedScoreCalculator(xi);
        return expectedScoreCalculator;
    }
}
