package com.github.wakingrufus.elo.elo;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by burnsj3 on 9/18/2014.
 */
public class ExpectedScoreCalculator {

    private int xi;

    public ExpectedScoreCalculator(int xi) {
        this.xi = xi;
    }

    public BigDecimal calculate(int rating1, int rating2) {
        BigDecimal q1 = calculateQ(rating1);
        BigDecimal q2 = calculateQ(rating2);
        return calculateE(q1, q2);
    }

    private BigDecimal calculateQ(int teamRating) {
        return BigDecimalHelper.pow(BigDecimal.TEN, new BigDecimal(teamRating).divide(new BigDecimal(xi), MathContext.DECIMAL32));
    }

    private BigDecimal calculateE(BigDecimal q1, BigDecimal q2) {
        return q1.divide(q1.add(q2), MathContext.DECIMAL32);
    }
}
