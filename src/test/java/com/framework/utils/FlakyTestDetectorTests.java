package com.framework.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

/** Unit coverage of {@link FlakyTestDetector}'s cross-run comparator. No browser involved. */
public class FlakyTestDetectorTests {

    private static final String SCENARIO_A = "com.example.Tests.methodA";
    private static final String SCENARIO_B = "com.example.Tests.methodB";

    @Test(description = "A scenario that passes in every run (current + priors) is never flagged flaky")
    public void shouldNotFlagAlwaysPassingScenario() {
        Set<String> flaky = FlakyTestDetector.computeFlakyScenarios(
                Map.of(SCENARIO_A, true),
                List.of(Map.of(SCENARIO_A, true), Map.of(SCENARIO_A, true)));

        Assert.assertTrue(flaky.isEmpty(), "Expected no flaky scenarios, got: " + flaky);
    }

    @Test(description = "A scenario that fails in every run (current + priors) is never flagged flaky")
    public void shouldNotFlagAlwaysFailingScenario() {
        Set<String> flaky = FlakyTestDetector.computeFlakyScenarios(
                Map.of(SCENARIO_A, false),
                List.of(Map.of(SCENARIO_A, false), Map.of(SCENARIO_A, false)));

        Assert.assertTrue(flaky.isEmpty(), "Expected no flaky scenarios, got: " + flaky);
    }

    @Test(description = "A scenario with a mix of pass and fail across current + prior runs is flagged flaky")
    public void shouldFlagInconsistentScenarioAcrossRuns() {
        Set<String> flaky = FlakyTestDetector.computeFlakyScenarios(
                Map.of(SCENARIO_A, true),
                List.of(Map.of(SCENARIO_A, false)));

        Assert.assertEquals(flaky, Set.of(SCENARIO_A));
    }

    @Test(description = "A scenario with no prior history is never flagged flaky on that basis alone")
    public void shouldNotFlagScenarioWithNoPriorHistory() {
        Set<String> flaky = FlakyTestDetector.computeFlakyScenarios(Map.of(SCENARIO_A, false), List.of());

        Assert.assertTrue(flaky.isEmpty(), "Expected no flaky scenarios, got: " + flaky);
    }

    @Test(description = "Distinct scenarios (e.g. different data-provider rows) are tracked independently")
    public void shouldKeyScenariosIndependently() {
        Map<String, Boolean> current = Map.of(SCENARIO_A, true, SCENARIO_B, false);
        List<Map<String, Boolean>> priors = List.of(Map.of(SCENARIO_A, true, SCENARIO_B, false));

        Set<String> flaky = FlakyTestDetector.computeFlakyScenarios(current, priors);

        Assert.assertTrue(flaky.isEmpty(), "Expected no flaky scenarios, got: " + flaky);
    }

    @Test(description = "recordResult keeps the latest outcome per scenario for the current run")
    public void shouldRecordCurrentRunOutcomes() {
        FlakyTestDetector detector = new FlakyTestDetector();
        detector.recordResult(SCENARIO_A, true);
        detector.recordResult(SCENARIO_B, false);

        Assert.assertEquals(detector.getCurrentOutcomes(), Map.of(SCENARIO_A, true, SCENARIO_B, false));
    }
}
