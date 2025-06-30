package io.swagger.petstore.v2.store;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.Matchers.*;

public class StoreApiTest {

    @BeforeAll
    static void setup() {
        baseURI = "https://petstore.swagger.io/v2";
        given().filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter());
    }


    @Test
    void すべて正しくボディを指定した場合_成功レスポンスが返ってくること() {
        var requestBody = """
            {
              "id": 0,
              "petId": 0,
              "quantity": 0,
              "shipDate": "2024-07-01T00:00:00.000+00:00",
              "status": "placed",
              "complete": true
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/store/order")
        .then()
            .statusCode(200)
            .body("id", allOf(notNullValue(), is(instanceOf(Long.class))));
    }
}
