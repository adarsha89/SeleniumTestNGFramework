package com.framework.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Entry point for on-demand tasks invoked via {@code exec:java}, e.g.:
 * {@code mvn -q compile exec:java -Dexec.args="impact-analysis --changed-area=cart"}
 * {@code mvn -q compile exec:java -Dexec.args="flaky-report"}
 * Neither subcommand runs any tests or opens a browser - both are read-only reporting tasks.
 */
@Command(name = "selenium-testng-framework", mixinStandardHelpOptions = true,
        subcommands = {ImpactAnalysisCommand.class, FlakyReportCommand.class})
public final class CliMain {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CliMain()).execute(args);
        System.exit(exitCode);
    }
}
