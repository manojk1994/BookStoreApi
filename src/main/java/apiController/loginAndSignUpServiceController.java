package apiController;

import data.BookStoreData;
import constants.EndPoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class loginAndSignUpServiceController {

    /**
     * Generates a random alphanumeric string of the given length.
     */
    public static String generateEmailAndPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return sb.toString();
    }

    /**
     * Signs up a new user, stores the returned userId into BookStoreData, and returns the raw Response.
     */
    public static Response signUp(String email, String password, BookStoreData bookStoreData) {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}")
                        .log().all()
                        .when()
                        .post(EndPoints.SIGN_UP)
                        .then()
                        .log().all()
                        .extract().response();

        // capture the userId for later reuse
        bookStoreData.setUserId(response.jsonPath().getString("userId"));
        return response;
    }

    /**
     * Logs in an existing user, stores the returned token into BookStoreData, and returns the raw Response.
     */
    public static Response login(String email, String password, BookStoreData bookStoreData) {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}")
                        .log().all()
                        .when()
                        .post(EndPoints.LOG_IN)
                        .then()
                        .log().all()
                        .extract().response();

        // capture the JWT token for authenticated calls
        bookStoreData.setToken(response.jsonPath().getString("token"));
        return response;
    }
}
