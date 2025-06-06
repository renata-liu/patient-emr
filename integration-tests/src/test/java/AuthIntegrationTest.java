import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnOkWithValidToken() {
        String loginPayload = """
                    {
                        "email": "testuser@test.com",
                        "password": "password123"
                    }
                """;
        Response response = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .response();
        System.out.println("Generated token: " + response.getBody().jsonPath().getString("token") + "");
    }

    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin() {
        String loginPayload = """
                    {
                        "email": "invaliduser@test.com",
                        "password": "badpassword123"
                    }
                """;
        given()
            .contentType("application/json")
            .body(loginPayload)
            .when()
            .post("/auth/login")
            .then()
            .statusCode(401);
    }
}
