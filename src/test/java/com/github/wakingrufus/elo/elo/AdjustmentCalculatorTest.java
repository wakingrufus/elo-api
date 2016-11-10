package com.github.wakingrufus.elo.elo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

@Slf4j
public class AdjustmentCalculatorTest {
    @Test
    public void calculate() throws Exception {
        AdjustmentCalculator adjustmentCalculator = new AdjustmentCalculator();

        int actual = adjustmentCalculator.calculate(32, BigDecimal.ONE, new BigDecimal(".5"));
        Assert.assertEquals(16,actual);

        actual = adjustmentCalculator.calculate(32, BigDecimal.ZERO, new BigDecimal(".5"));
        Assert.assertEquals(-16, actual);
    }

}