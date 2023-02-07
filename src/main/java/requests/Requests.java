package requests;

import dto.Order;
import dto.Pet;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidatorSettings.settings;

public class Requests {

    public static String url = "https://petstore.swagger.io/v2";

    public static Pet postJsonPath(String endpoint, String schemePath, Pet newPet) {
        return given().
                filters(new AllureRestAssured()).
                log().all().
                contentType(ContentType.JSON).
                body(newPet).
                when().
                post(url+endpoint).
                then().log().all().
                statusCode(200).
                statusLine("HTTP/1.1 200 OK").
                contentType(ContentType.JSON).
                body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemePath).
                        using(settings().with().checkedValidation(true))).
                extract().response().as(Pet.class);
    }

    public static Order postOrderJsonPath(String endpoint, String schemePath, Order newOrder) {
        return given().
                filters(new AllureRestAssured()).
                log().all().
                contentType(ContentType.JSON).
                body(newOrder).
                when().
                post(url+endpoint).
                then().log().all().
                statusCode(200).
                statusLine("HTTP/1.1 200 OK").
                contentType(ContentType.JSON).
                body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemePath).
                        using(settings().with().checkedValidation(true))).
                extract().as(Order.class);
    }


    public static JsonPath getJsonPath(String endpoint, String schemePath) {
        return given().
                filters(new AllureRestAssured()).
                log().all().
                contentType(ContentType.JSON).
                when().
                get(url+endpoint).
                then().log().all().
                statusCode(200).
                statusLine("HTTP/1.1 200 OK").
                contentType(ContentType.JSON).
                body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemePath).
                        using(settings().with().checkedValidation(true))).
                extract().jsonPath();
    }

    public static Pet putJsonPath(String endpoint, String schemePath, Pet newPet) {
        return given().
                filters(new AllureRestAssured()).
                log().all().
                contentType(ContentType.JSON).
                body(newPet).
                when().
                put(url+endpoint).
                then().log().all().
                statusCode(200).
                statusLine("HTTP/1.1 200 OK").
                contentType(ContentType.JSON).
                body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemePath).
                        using(settings().with().checkedValidation(true))).
                extract().response().as(Pet.class);
    }


    public static JsonPath postNotFound(String endpoint) {
        return given().
                filters(new AllureRestAssured()).
                log().all().
                contentType("application/x-www-form-urlencoded; charset=UTF-8").
                body("1").
//                header("accept", "application/json").
                when().
                post(url+endpoint).
                then().log().all().
                statusCode(404).
                statusLine("HTTP/1.1 404 Not Found").
                contentType(ContentType.JSON).
                extract().jsonPath();
    }
}
