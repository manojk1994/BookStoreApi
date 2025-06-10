package apiController;

import data.BookStoreData;
import constants.EndPoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class bookApiServiceController {

    /**
     * Creates a new book under the current user, stores bookId in BookStoreData, and returns the Response.
     */
    public static Response createBook(Map<String, Object> bookDetails, String token, BookStoreData bookStoreData) {
        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .contentType(ContentType.JSON)
                        .body(bookDetails)
                        .log().all()
                        .when()
                        .post(EndPoints.ADD_NEW_BOOK)
                        .then()
                        .log().all()
                        .extract().response();

        // capture the bookId for subsequent retrieval/deletion
        bookStoreData.setBookId(response.jsonPath().getString("bookId"));
        return response;
    }

    /**
     * Retrieves a book by its ID. Returns the raw Response.
     */
    public static Response getBookById(String bookId, String token) {
        return
                given()
                        .header("Authorization", "Bearer " + token)
                        .accept(ContentType.JSON)
                        .log().all()
                        .when()
                        .get(EndPoints.BY_BOOK_ID.replace("{book_id}", bookId))
                        .then()
                        .log().all()
                        .extract().response();
    }

    /**
     * Deletes a book by its ID. Returns the raw Response.
     */
    public static Response deleteBook(String bookId, String token) {
        return
                given()
                        .header("Authorization", "Bearer " + token)
                        .accept(ContentType.JSON)
                        .log().all()
                        .when()
                        .delete(EndPoints.BY_BOOK_ID.replace("{book_id}", bookId))
                        .then()
                        .log().all()
                        .extract().response();
    }
}
