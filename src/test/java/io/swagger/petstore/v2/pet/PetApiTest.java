package io.swagger.petstore.v2.pet;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.Matchers.*;

public class PetApiTest {

    @BeforeAll
    static void setup() {
        baseURI = "https://petstore.swagger.io/v2";
        given().filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter());
    }


    @Test
    void すべて正しくボディを指定した場合_成功レスポンスが返ってくること() {
        var requestBody = """
            {
              "category": {
                "id": 1,
                "name": "Dogs"
              },
              "name": "doggie",
              "photoUrls": [
                "string"
              ],
              "tags": [
                {
                  "id": 0,
                  "name": "string"
                }
              ],
              "status": "available"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/pet")
        .then()
            .statusCode(200)
            .body("id", allOf(notNullValue(), is(instanceOf(Long.class))))
            .body("name", equalTo("doggie"))
            .body("status", equalTo("available"));
    }

    @Test
    void すべて正しくボディを指定した場合_ペットが更新されること() {
        // 事前にペットを追加
        var addRequestBody = """
            {
              "category": {
                "id": 1,
                "name": "Dogs"
              },
              "name": "doggie",
              "photoUrls": [
                "string"
              ],
              "tags": [
                {
                  "id": 0,
                  "name": "string"
                }
              ],
              "status": "available"
            }
            """;

        var petId = given()
            .contentType(ContentType.JSON)
            .body(addRequestBody)
        .when()
            .post("/pet")
        .then()
            .statusCode(200)
            .extract().path("id");

        // ペットを更新
        var updateRequestBody = String.format("""
            {
              "id": %s,
              "category": {
                "id": 1,
                "name": "Dogs"
              },
              "name": "doggie_updated",
              "photoUrls": [
                "string_updated"
              ],
              "tags": [
                {
                  "id": 0,
                  "name": "string_updated"
                }
              ],
              "status": "sold"
            }
            """, petId);

        given()
            .contentType(ContentType.JSON)
            .body(updateRequestBody)
        .when()
            .put("/pet")
        .then()
            .statusCode(200)
            .body("id", equalTo(petId))
            .body("name", equalTo("doggie_updated"));
    }
}