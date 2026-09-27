package com.framework.config;

/** Central place for saucedemo.com's fixed fixtures (user accounts, sample checkout data). */
public final class TestData {

    private TestData() {
    }

    public static final String PASSWORD = ConfigReader.get("password");
    public static final String STANDARD_USER = ConfigReader.get("standard.username");
    public static final String LOCKED_OUT_USER = ConfigReader.get("locked.out.username");
    public static final String PROBLEM_USER = ConfigReader.get("problem.username");
    public static final String PERFORMANCE_GLITCH_USER = ConfigReader.get("performance.glitch.username");

    public static final String SAMPLE_FIRST_NAME = "Jane";
    public static final String SAMPLE_LAST_NAME = "Doe";
    public static final String SAMPLE_POSTAL_CODE = "94107";
}
