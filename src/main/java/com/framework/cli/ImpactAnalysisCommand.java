package com.framework.cli;

import com.framework.utils.CliHtmlReportWriter;
import com.framework.utils.Constants;
import com.framework.utils.TestImpactAnalyzer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/** Prints the test classes impacted by a set of changed areas, per {@link TestImpactAnalyzer}, and
 * writes the same result to an HTML report. With no {@code --changed-area} given, falls back to
 * every test class in {@code src/main/resources/testimpact/dependency-graph.json}. */
@Command(name = "impact-analysis", description = "Print the test classes impacted by the given changed area(s)")
public final class ImpactAnalysisCommand implements Runnable {

    @Spec
    private CommandSpec spec;

    @Option(names = "--changed-area", split = ",",
            description = "Changed area name(s), comma-separated or repeated. Defaults to every test class in "
                    + "the dependency graph when omitted.")
    private List<String> changedAreas;

    @Override
    public void run() {
        TestImpactAnalyzer analyzer = new TestImpactAnalyzer();
        Set<String> impacted = new TreeSet<>((changedAreas == null || changedAreas.isEmpty())
                ? analyzer.getAllTestClasses()
                : analyzer.getTestcasesImpacted(changedAreas));

        PrintWriter out = spec.commandLine().getOut();
        impacted.forEach(out::println);
        CliHtmlReportWriter.write(Path.of(Constants.IMPACT_ANALYSIS_REPORT_FILE), "Impact Analysis Report", impacted);
    }
}
