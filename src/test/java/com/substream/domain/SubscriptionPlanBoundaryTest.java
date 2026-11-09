package com.substream.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscriptionPlanBoundaryTest {

    @ParameterizedTest
    @CsvSource({"FREE, 0.00", "PRO, 29.00", "ENTERPRISE, 199.00"})
    void pricesMatchThePlanTableWithTwoDecimalPlaces(SubscriptionPlan plan, String price) {
        assertEquals(new BigDecimal(price), plan.getMonthlyPrice());
    }

    @ParameterizedTest
    @CsvSource({
            "FREE, -2147483648, false", "FREE, 0, false", "FREE, 1, true", "FREE, 2, false",
            "PRO, -2147483648, false", "PRO, 1, true", "PRO, 9, true", "PRO, 10, true", "PRO, 11, false",
            "ENTERPRISE, -2147483648, false", "ENTERPRISE, 0, false", "ENTERPRISE, 1, true",
            "ENTERPRISE, 2147483647, true"
    })
    void seatLimitsIncludeBothBoundaries(SubscriptionPlan plan, int seats, boolean expected) {
        assertEquals(expected, plan.canAccommodate(seats));
    }

    @ParameterizedTest
    @CsvSource({
            "FREE, FREE, false", "FREE, PRO, false", "FREE, ENTERPRISE, false",
            "PRO, FREE, true", "PRO, PRO, false", "PRO, ENTERPRISE, false",
            "ENTERPRISE, FREE, true", "ENTERPRISE, PRO, true", "ENTERPRISE, ENTERPRISE, false"
    })
    void upgradesFollowTheTierOrder(SubscriptionPlan target, SubscriptionPlan current, boolean expected) {
        assertEquals(expected, target.isUpgradeFrom(current));
    }
}
