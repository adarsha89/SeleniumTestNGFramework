package com.framework.utils;

import org.testng.TestNG;

import java.util.List;

public class CustomTestRunner {
    public static void main(String[] args) {
        TestNG testNG = new TestNG();
        testNG.setTestSuites(List.of());
        testNG.run();
    }
}
