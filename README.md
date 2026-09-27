# Selenium + TestNG Framework — saucedemo.com

Parallel-ready UI automation framework built with **Selenium 4.25** and **TestNG 7.10**, targeting
https://www.saucedemo.com/.

## Highlights

- **Selenium Manager** (built into Selenium 4.6+) resolves browser drivers automatically — no
  WebDriverManager dependency, no manual driver downloads.
- **Thread-safe parallel execution**: `DriverFactory` hands each TestNG thread its own `WebDriver`
  via `ThreadLocal`, so `testng.xml` can run classes (or methods) concurrently without cross-test
  contamination.
- **Page Object Model** with `PageFactory` and a shared `BasePage` providing wait-wrapped
  interactions (`click`, `type`, `getText`, ...).
- **Fluent explicit waits** (`WaitUtils`) instead of blanket implicit waits.
- Config-driven via `config.properties`, overridable with `-D` system properties for CI.
- Screenshot-on-failure via a TestNG `ITestListener`.

## Project layout

```
src/main/java/com/framework/
  config/     ConfigReader, TestData
  driver/     DriverFactory (ThreadLocal WebDriver, Selenium Manager based)
  pages/      Page Objects (LoginPage, InventoryPage, CartPage, Checkout*Page),
              BasePage, LoggedInPage (pages reachable after login expose header())
    modules/  Reusable page modules shared across pages (HeaderModule,
              BurgerMenuModule, ErrorMessageModule)
  assertions/ Verifications per area (LoginAssertions, InventoryAssertions, ...)
  services/   Business flows that drive pages and call assertions (LoginService, ...)
  utils/      WaitUtils, ScreenshotUtils, FlakyTestDetector, RunHistoryStore,
              TestImpactAnalyzer, CliHtmlReportWriter
  listeners/  TestListener (screenshot on failure, cross-run flaky detection)
  cli/        CliMain + flaky-report / impact-analysis subcommands (picocli)

src/main/resources/
  testimpact/dependency-graph.json   test class -> functional areas it covers
  scripts/    flaky-report.sh, impact-analysis.sh (run CLI + open HTML report)

src/test/java/com/framework/tests/
  base/BaseTest.java   per-method driver lifecycle
  LoginTest, InventoryTest, CheckoutTest

src/test/resources/suites/testng.xml   suite definition with parallel="classes" thread-count="10"
```

## Running the suite

```bash
mvn test
```

### Override browser / headless / thread-count

```bash
mvn test -Dbrowser=firefox -Dheadless=true -Dthread.count=5
```

### Run a single test class

```bash
mvn test -Dtest=LoginTest
```

Screenshots for failed tests are written to `target/screenshots/`.

## Flaky test detection

`TestListener` records every scenario's outcome (method + data-provider parameters) during a run.
At the end of the suite it compares the run against the last 10 runs stored in
`src/test/resources/data/previousTestResults/` and flags any scenario that has both passed and
failed across those runs. Results are written to `target/test-result-summary.json` and logged as
`[FLAKY]`. Override the history directory with `-DrunHistoryStore.directory=...`.

Report on stored history without running any tests or browsers:

```bash
mvn -q compile exec:java -Dexec.args="flaky-report"
# or: src/main/resources/scripts/flaky-report.sh   (also opens target/cli-reports/flaky-report.html)
```

## Test impact analysis

`TestImpactAnalyzer` maps changed areas to the test classes that cover them, using
`src/main/resources/testimpact/dependency-graph.json` (areas: `driver`, `config`, `login`,
`inventory`, `cart`, `checkout`). Update that file when you add a test class.

```bash
mvn -q compile exec:java -Dexec.args="impact-analysis --changed-area=cart,checkout"
# or: src/main/resources/scripts/impact-analysis.sh --changed-area=cart
```

With no `--changed-area`, every test class is listed. The HTML report is written to
`target/cli-reports/impact-analysis-report.html`.

## Layering: tests → services → pages / assertions

Tests **never** call a page object or an assertion class directly. They only talk to services:

- **Pages** know locators and UI interactions, and return raw values.
- **Assertions** verify values handed to them (they never touch pages or the driver).
- **Services** orchestrate a flow: they drive pages and pass what they read to assertions.
  Methods return the service itself so calls chain fluently, e.g.
  `loginService.loginAsStandardUser().verifyLoggedIn();`

## Adding a new page/flow

1. Create a `XyzPage implements BasePage` (or `LoggedInPage` if it's behind login) in `pages/` with `By` locators and public actions.
2. Add `XyzAssertions` in `assertions/` for any new verifications.
3. Add (or extend) `XyzService` in `services/` exposing the actions and `verify*` methods tests need.
4. Add a test class under `tests/` extending `BaseTest` that uses only services, and register it
   in `src/test/resources/suites/testng.xml` and `src/main/resources/testimpact/dependency-graph.json`.

UI fragments that appear on several pages (header, menus, banners) belong in a page module under
`pages/modules/`, exposed from pages via an accessor such as `header()`, not duplicated per page.
