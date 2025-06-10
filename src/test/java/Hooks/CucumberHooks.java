package Hooks;

import io.cucumber.java.Before;
import io.restassured.RestAssured;
import utils.configController;

public class CucumberHooks {

    @Before
    public void setup() {
        String env = System.getProperty("env", "mock");
        if (!"mock".equals(env)) {
            // Real API: load from properties
            configController.setUp();
            RestAssured.baseURI = configController.get("base.url") + ":" + configController.get("base.port");
        }
        // else, WireMockHooks already configured RestAssured.baseURI
    }
}