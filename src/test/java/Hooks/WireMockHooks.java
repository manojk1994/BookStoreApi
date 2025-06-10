package Hooks;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import io.restassured.RestAssured;
import org.testng.IExecutionListener;

public class WireMockHooks implements IExecutionListener {
    private static WireMockServer wireMockServer;

    @Override
    public void onExecutionStart() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        System.out.println(">>> WireMock listening on port: " + wireMockServer.port());
        RestAssured.baseURI = "http://localhost:" + wireMockServer.port();
    }

    @Override
    public void onExecutionFinish() {
        if (wireMockServer != null) {
            wireMockServer.stop();
            System.out.println(">>> WireMock stopped");
        }
    }
}
