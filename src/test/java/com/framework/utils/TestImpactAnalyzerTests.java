package com.framework.utils;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

/** Verifies {@link TestImpactAnalyzer} against the real committed dependency-graph.json. */
public class TestImpactAnalyzerTests {

    private final TestImpactAnalyzer analyzer = new TestImpactAnalyzer();

    @Test(dataProvider = "changedAreasAndExpectedTests",
            description = "getTestcasesImpacted returns exactly the expected test classes - no extra, no missing")
    public void shouldReturnExactExpectedTestsForKnownChangedAreas(String caseName, List<String> changedAreas,
            Set<String> expectedTestNames) {
        Assert.assertEquals(analyzer.getTestcasesImpacted(changedAreas), expectedTestNames, "Case: " + caseName);
    }

    @Test(description = "A changed area with no mapped tests returns an empty set, no exception")
    public void shouldReturnEmptySetForUnmappedArea() {
        Assert.assertTrue(analyzer.getTestcasesImpacted(List.of("nonexistent-area")).isEmpty());
    }

    @Test(description = "getAllTestClasses returns every test class in the dependency graph")
    public void shouldReturnAllTestClasses() {
        Assert.assertEquals(analyzer.getAllTestClasses(), Set.of("LoginTest", "InventoryTest", "CheckoutTest"));
    }

    @DataProvider(name = "changedAreasAndExpectedTests")
    public Object[][] changedAreasAndExpectedTests() {
        return new Object[][]{
                {"area covered by one test", List.of("checkout"), Set.of("CheckoutTest")},
                {"area shared by two tests", List.of("inventory"), Set.of("InventoryTest", "CheckoutTest")},
                {"shared infrastructure area", List.of("driver"),
                        Set.of("LoginTest", "InventoryTest", "CheckoutTest")},
                {"multiple areas, union of mapped tests", List.of("cart", "inventory"),
                        Set.of("InventoryTest", "CheckoutTest")},
        };
    }
}
