package com.hazelsuite.utilities;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class Listeners implements ITestListener {


    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        Log.info("================" + getTestMethodName(iTestResult) + " Test is starting.================");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        Log.info(getTestMethodName(iTestResult) + " Test completed successfully.");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        Log.error(getTestMethodName(iTestResult) + " Test Failed");
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        Log.warn(getTestMethodName(iTestResult) + " Test is skipped.");

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        Log.warn("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        Log.info("Starting Execution");


    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        Log.info("Execution Completed. Closing Browser");


    }
}