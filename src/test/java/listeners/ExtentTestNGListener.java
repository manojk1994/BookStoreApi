package listeners;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import io.restassured.RestAssured;

import org.testng.IExecutionListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class ExtentTestNGListener implements ITestListener, IExecutionListener {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    private static WireMockServer wireMockServer;

    @Override
    public void onExecutionStart() {
        // --- start WireMock ---
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        int port = wireMockServer.port();
        List<StubMapping> mappings = wireMockServer.getStubMappings();
        System.out.println(">>> WireMock listening on port: " + port);
        System.out.println(">>> Stub mappings loaded: " + mappings.size());
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.defaultParser = io.restassured.parsing.Parser.JSON;

        // --- init ExtentReports ---
        ExtentSparkReporter spark = new ExtentSparkReporter("target/extent-report.html");
        try {
            spark.loadXMLConfig("extent-config.xml");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load extent-config.xml", e);
        }
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Use TestNG’s name (Cucumber runner sets this to the scenario’s name)
        String scenarioName = result.getName();
        ExtentTest test = extent.createTest(scenarioName);
        testThread.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testThread.get().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testThread.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testThread.get().skip(result.getThrowable());
    }

    @Override
    public void onExecutionFinish() {
        extent.flush();
        if (wireMockServer != null) {
            wireMockServer.stop();
            System.out.println(">>> WireMock stopped");
        }
    }
}
