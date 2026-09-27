package com.framework.utils;

/** File locations shared by the flaky-test / impact-analysis utilities, the listener, and the CLI. */
public final class Constants {

    private Constants() {
    }

    // Cross-run flaky detection (see RunHistoryStore / TestListener)
    public static final String RUN_HISTORY_DIR = "src/test/resources/data/previousTestResults";
    public static final String RUN_HISTORY_DIR_PROPERTY = "runHistoryStore.directory";
    public static final String TEST_RESULT_SUMMARY_FILE = "target/test-result-summary.json";

    // CLI HTML report output (see ImpactAnalysisCommand / FlakyReportCommand)
    public static final String CLI_REPORTS_DIR = "target/cli-reports";
    public static final String IMPACT_ANALYSIS_REPORT_FILE = CLI_REPORTS_DIR + "/impact-analysis-report.html";
    public static final String FLAKY_REPORT_FILE = CLI_REPORTS_DIR + "/flaky-report.html";
}
