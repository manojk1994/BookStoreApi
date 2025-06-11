package listeners;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import io.restassured.RestAssured;

import org.testng.IExecutionListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class ExtentTestNGListener implements ITestListener, IExecutionListener {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    private static WireMockServer wireMockServer;

    @Override
    public void onExecutionStart() {
        // 1) Compute a timestamped folder & file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportDir  = "target/extent-reports/run-" + timestamp;
        String reportPath = reportDir + "/Spark.html";

        // 2) Make sure the directory exists
        File dir = new File(reportDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Could not create report directory: " + reportDir);
        }

        // 3) Create the reporter against that path
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        try {
            spark.loadXMLConfig("extent-config.xml");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load extent-config.xml", e);
        }

        // 4) Attach it
        extent = new ExtentReports();
        extent.attachReporter(spark);

        // 5) (rest of your setup: start WireMock, set baseURI, etc.)
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        int port = wireMockServer.port();
        System.out.println(">>> WireMock on port: " + port +
                ", loaded stubs: " + wireMockServer.getStubMappings().size());
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.defaultParser = io.restassured.parsing.Parser.JSON;
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
