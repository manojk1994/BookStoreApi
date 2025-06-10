package Steps;

import apiController.loginAndSignUpServiceController;
import apiController.bookApiServiceController;
import data.BookStoreData;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.Assert.*;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class UserFlowSteps {
    private BookStoreData data = new BookStoreData();
    private String email;
    private String password;
    private Response lastResponse;

    @Given("the email {string} and password {string}")
    public void setCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @When("the user signs up with those credentials")
    public void signupWithCredentials() {
        lastResponse = loginAndSignUpServiceController.signUp(email, password, data);
    }

    @When("the user logs in with those credentials")
    public void loginWithCredentials() {
        lastResponse = loginAndSignUpServiceController.login(email, password, data);
    }

    @When("user retrieves the book with id {string}")
    public void getBookWithId(String bookId) {
        lastResponse = bookApiServiceController.getBookById(bookId, data.getToken());
    }

    @Then("the API should return status {int} and message {string}")
    public void verifyErrorResponse(int statusCode, String message) {
        assertEquals(lastResponse.getStatusCode(), statusCode, "Unexpected status");
        assertEquals(lastResponse.jsonPath().getString("message"), message, "Unexpected error message");
    }
    @Given("generate random credentials")
    public void generateCredentials() {
        email = loginAndSignUpServiceController.generateEmailAndPassword(8) + "@mail.com";
        password = loginAndSignUpServiceController.generateEmailAndPassword(8);
    }

    @When("the user signs up with valid credentials")
    public void userSignsUp() {
        Response resp = loginAndSignUpServiceController.signUp(email, password, data);
        Assert.assertEquals(resp.getStatusCode(), 201);
    }

    @When("the user logs in with valid credentials")
    public void userLogsIn() {
        Response resp = loginAndSignUpServiceController.login(email, password, data);
        Assert.assertEquals(resp.getStatusCode(), 200);
    }

    @Then("the login token should be returned")
    public void loginTokenReturned() {
        Assert.assertNotNull(data.getToken());
    }

    @Given("user is logged in")
    public void userLoggedIn() {
        generateCredentials();
        userSignsUp();
        userLogsIn();
    }

    @When("user creates a new book")
    public void createBook() {
        Map<String, Object> bookDetails = new HashMap<>();
        bookDetails.put("userId", data.getUserId());
        bookDetails.put("isbn", "9781449325862");
        bookDetails.put("title", "Learn API Testing");
        bookDetails.put("author", "John Doe");
        Response resp = bookApiServiceController.createBook(bookDetails, data.getToken(), data);
        Assert.assertEquals(resp.getStatusCode(), 201);
    }

    @Then("bookId is returned")
    public void bookIdReturned() {
        Assert.assertNotNull(data.getBookId());
    }

    @When("user retrieves the book by id")
    public void getBook() {
        Response resp = bookApiServiceController.getBookById(data.getBookId(), data.getToken());
        Assert.assertEquals(resp.getStatusCode(), 200);
    }

    @Then("book details are returned")
    public void bookDetailsReturned() {
        Assert.assertNotNull(data.getBookId());
    }

    @When("user deletes the book by id")
    public void deleteBook() {
        Response resp = bookApiServiceController.deleteBook(data.getBookId(), data.getToken());
        Assert.assertEquals(resp.getStatusCode(), 204);
    }

    @Then("the book is deleted")
    public void bookDeleted() {
        // no additional assertions
    }
}