package com.gendev.streamcombinations.util;

import com.gendev.streamcombinations.model.main.StreamingPackage;

public class SubscriptionCostUtil {

    public static int calculateTotalCost(StreamingPackage sp, int monthsDifference) {
        int monthlyPrice = sp.getMonthly_price_cents();
        int yearlyMonthlyPrice = sp.getMonthly_price_yearly_subscription_in_cents();

        if (monthsDifference < 0) {
            return yearlyMonthlyPrice * 12;
        }

        if (monthsDifference <= 12) {
            if (monthlyPrice != 7777) {
                return monthlyPrice * monthsDifference;
            } else {
                return yearlyMonthlyPrice * 12;
            }
        } else {
            int years = (int) Math.ceil(monthsDifference / 12.0);
            return yearlyMonthlyPrice * 12 * years;
        }
    }
}
