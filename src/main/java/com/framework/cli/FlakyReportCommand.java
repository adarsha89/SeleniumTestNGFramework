package com.framework.cli;

import com.framework.utils.CliHtmlReportWriter;
import com.framework.utils.Constants;
import com.framework.utils.FlakyTestDetector;
import com.framework.utils.RunHistoryStore;
import picocli.CommandLine.Command;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

/**
 * Prints the scenarios the cross-run comparator would flag as flaky, without running any tests,
 * and writes the same result to an HTML report. With no live run to compare against, the
 * most-recently persisted {@code run-*.json} file stands in as "current" and every other stored
 * file as "priors" - the same comparator {@code TestListener#onFinish} uses given that history.
 */
@Command(name = "flaky-report", description = "Print scenarios flagged flaky from stored run history")
public final class FlakyReportCommand implements Callable<Integer> {

    private static final String REPORT_TITLE = "Flaky Test Report";

    private final RunHistoryStore runHistoryStore;

    FlakyReportCommand() {
        this(new RunHistoryStore());
    }

    FlakyReportCommand(RunHistoryStore runHistoryStore) {
        this.runHistoryStore = runHistoryStore;
    }

    @Override
    public Integer call() {
        List<Map<String, Boolean>> outcomeSets = runHistoryStore.load();
        if (outcomeSets.size() < 2) {
            System.out.println("Not enough run history to detect flakiness (need at least 2 runs, found "
                    + outcomeSets.size() + ")");
            CliHtmlReportWriter.write(Path.of(Constants.FLAKY_REPORT_FILE), REPORT_TITLE, Set.of());
            return 0;
        }

        Map<String, Boolean> current = outcomeSets.get(0);
        List<Map<String, Boolean>> priors = outcomeSets.subList(1, outcomeSets.size());

        Set<String> flaky = new TreeSet<>(FlakyTestDetector.computeFlakyScenarios(current, priors));
        flaky.forEach(System.out::println);
        CliHtmlReportWriter.write(Path.of(Constants.FLAKY_REPORT_FILE), REPORT_TITLE, flaky);
        return 0;
    }
}
