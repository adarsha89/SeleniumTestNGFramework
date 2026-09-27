package com.framework.utils;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/** Tests {@link RunHistoryStore} directly against a temporary directory. */
public class RunHistoryStoreTests {

    private static final String SCENARIO_A = "com.example.Tests.methodA";

    private Path historyDirectory;

    @AfterMethod(alwaysRun = true)
    public void cleanUpHistoryDirectory() throws IOException {
        if (historyDirectory != null && Files.exists(historyDirectory)) {
            try (Stream<Path> paths = Files.walk(historyDirectory)) {
                paths.sorted(Comparator.reverseOrder()).forEach(RunHistoryStoreTests::deleteQuietly);
            }
        }
        historyDirectory = null;
    }

    @Test(description = "load() returns an empty history (no exception) when the directory doesn't exist")
    public void shouldReturnEmptyHistoryForMissingDirectory() throws IOException {
        historyDirectory = Files.createTempDirectory("run-history-missing");
        RunHistoryStore store = new RunHistoryStore(historyDirectory.resolve("does-not-exist"));

        Assert.assertTrue(store.load().isEmpty());
    }

    @Test(description = "load() skips an unreadable/malformed prior-run file rather than throwing")
    public void shouldSkipMalformedPriorRunFile() throws IOException {
        historyDirectory = Files.createTempDirectory("run-history-malformed");
        Files.writeString(historyDirectory.resolve("run-bad.json"), "not valid json {{{", StandardCharsets.UTF_8);

        Assert.assertTrue(new RunHistoryStore(historyDirectory).load().isEmpty());
    }

    @Test(description = "Consecutive persist() calls write distinct run files that load() reads back")
    public void shouldPersistAndLoadDistinctRuns() throws IOException {
        historyDirectory = Files.createTempDirectory("run-history-roundtrip");
        RunHistoryStore store = new RunHistoryStore(historyDirectory);

        store.persist(Map.of(SCENARIO_A, true));
        store.persist(Map.of(SCENARIO_A, false));

        List<Map<String, Boolean>> loaded = store.load();
        Assert.assertEquals(loaded.size(), 2, "Expected each persist() call to write a distinct file");
        Assert.assertTrue(loaded.contains(Map.of(SCENARIO_A, true)));
        Assert.assertTrue(loaded.contains(Map.of(SCENARIO_A, false)));
    }

    @Test(description = "persist() prunes history to the 10 most recent runs")
    public void shouldPruneToMostRecentRuns() throws IOException {
        historyDirectory = Files.createTempDirectory("run-history-prune");
        RunHistoryStore store = new RunHistoryStore(historyDirectory);

        for (int i = 0; i < 12; i++) {
            store.persist(Map.of(SCENARIO_A, i % 2 == 0));
        }

        Assert.assertEquals(store.load().size(), 10);
    }

    private static void deleteQuietly(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            // best-effort cleanup, nothing to act on if it fails
        }
    }
}
