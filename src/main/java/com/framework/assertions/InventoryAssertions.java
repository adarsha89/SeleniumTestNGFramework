package com.framework.assertions;

import org.testng.Assert;

import java.util.List;

/** Verifications for the inventory (products) page. */
public class InventoryAssertions {

    public void verifyPricesSortedAscending(List<Double> prices) {
        List<Double> sorted = prices.stream().sorted().toList();
        Assert.assertEquals(prices, sorted, "Prices should be sorted ascending");
    }

    public void verifyNamesSortedDescending(List<String> names) {
        List<String> sorted = names.stream().sorted((a, b) -> b.compareToIgnoreCase(a)).toList();
        Assert.assertEquals(names, sorted, "Names should be sorted descending");
    }

    public void verifyCartBadgeCount(int actualCount, int expectedCount) {
        Assert.assertEquals(actualCount, expectedCount, "Cart badge should reflect " + expectedCount + " item(s)");
    }
}
