package com.framework.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.driver.DriverManager;
import com.framework.utils.Constants;
import com.framework.utils.FlakyTestDetector;
import com.framework.utils.RunHistoryStore;
import com.framework.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Captures a screenshot for every failed test and logs pass/fail/skip. Also feeds every result
 * into a {@link FlakyTestDetector}; at the end of the suite it compares this run against prior
 * runs persisted via {@link RunHistoryStore}, writes the flaky scenarios and this run's outcomes
 * to {@value Constants#TEST_RESULT_SUMMARY_FILE}, and persists this run for future comparisons.
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    private final FlakyTestDetector flakyTestDetector = new FlakyTestDetector();
    private final RunHistoryStore runHistoryStore = new RunHistoryStore();

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            String path = ScreenshotUtils.capture(driver, testName);
            log.error("[FAIL] {} - screenshot saved to {}", testName, path);
        } else {
            log.error("[FAIL] {} - no active browser session, screenshot skipped", testName);
        }
        recordOutcome(result, false);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("[PASS] {}", result.getMethod().getMethodName());
        recordOutcome(result, true);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("[SKIP] {}", result.getMethod().getMethodName());
        recordOutcome(result, false);
    }

    @Override
    public void onFinish(ISuite suite) {
        Map<String, Boolean> currentOutcomes = flakyTestDetector.getCurrentOutcomes();
        if (currentOutcomes.isEmpty()) {
            return;
        }
        Set<String> flakyScenarios = new TreeSet<>(
                FlakyTestDetector.computeFlakyScenarios(currentOutcomes, runHistoryStore.load()));
        flakyScenarios.forEach(scenario -> log.warn("[FLAKY] {}", scenario));

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("flakyTests", flakyScenarios);
        summary.put("scenarioOutcomes", currentOutcomes);

        File summaryFile = new File(Constants.TEST_RESULT_SUMMARY_FILE);
        File parentDir = summaryFile.getParentFile();
        if (parentDir != null) {
            parentDir.mkdirs();
        }
        try {
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(summaryFile, summary);
        } catch (IOException e) {
            log.warn("Failed to write test result summary to {}", summaryFile, e);
        }

        runHistoryStore.persist(currentOutcomes);
    }

    private void recordOutcome(ITestResult result, boolean passed) {
        flakyTestDetector.recordResult(scenarioId(result), passed);
    }

    /** Qualified method name plus parameters, so different data-provider rows of the same method
     * are tracked as distinct scenarios rather than sharing one history. */
    private static String scenarioId(ITestResult result) {
        String testName = result.getMethod().getQualifiedName();
        Object[] parameters = result.getParameters();
        if (parameters == null || parameters.length == 0) {
            return testName;
        }
        return testName + "::" + Arrays.deepToString(parameters);
    }
}
