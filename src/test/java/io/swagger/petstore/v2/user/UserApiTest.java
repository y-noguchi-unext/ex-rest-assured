package io.swagger.petstore.v2.user;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.Matchers.*;

public class UserApiTest {

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
                  "username": "testuser",
                  "firstName": "Test",
                  "lastName": "User",
                  "email": "test@example.com",
                  "password": "password",
                  "phone": "123-456-7890",
                  "userStatus": 0
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/user")
                .then()
            .statusCode(200)
            .body("code", equalTo(200))
            .body("type", equalTo("unknown"))
            .body("message", allOf(notNullValue(), is(instanceOf(String.class))));
    }

    @Test
    void 存在するユーザー名を指定した場合_ユーザー情報が返ってくること() {
        // 事前にユーザーを作成
        var createRequestBody = """
                {
                  "id": 0,
                  "username": "testuser_get",
                  "firstName": "Test",
                  "lastName": "User",
                  "email": "test_get@example.com",
                  "password": "password_get",
                  "phone": "123-456-7890",
                  "userStatus": 0
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(createRequestBody)
        .when()
            .post("/user")
        .then()
            .statusCode(200);

        // ユーザー名でユーザーを取得
        given()
        .when()
            .get("/user/{username}", "testuser_get")
        .then()
            .statusCode(200)
                .body("username", equalTo("testuser_get"));
    }
}