package requests;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidatorSettings.*;

import dto.Order;
import dto.Pet;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Requests {

  private static String url;

  static {
    try {
      url = getConfigPath("base.url");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getConfigPath(String path) throws IOException {
    Properties properties = new Properties();
    properties.load(new FileInputStream("src/test/resources/config.properties"));
    return properties.getProperty(path);
  }


  public static Pet postNewPet(String endpoint, String schemePath, Pet newPet) {
    return given()
        .filters(new AllureRestAssured())
        .log()
        .all()
        .contentType(ContentType.JSON)
        .relaxedHTTPSValidation()
        .body(newPet)
        .when()
        .post(url + endpoint)
        .then()
        .log()
        .all()
        .statusCode(200)
        .statusLine("HTTP/1.1 200 OK")
        .contentType(ContentType.JSON)
        .body(JsonSchemaValidator
            .matchesJsonSchemaInClasspath(schemePath)
            .using(settings()
                .with()
                .checkedValidation(true)))
        .extract()
        .response()
        .as(Pet.class);
  }

  public static Order postNewOrder(String endpoint, String schemePath, Order newOrder) {
    return given()
        .filters(new AllureRestAssured())
        .relaxedHTTPSValidation()
        .log()
        .all()
        .contentType(ContentType.JSON)
        .body(newOrder)
        .when()
        .post(url + endpoint)
        .then()
        .log()
        .all()
        .statusCode(200)
        .statusLine("HTTP/1.1 200 OK")
        .contentType(ContentType.JSON)
        .body(JsonSchemaValidator
            .matchesJsonSchemaInClasspath(schemePath)
            .using(settings()
                .with()
                .checkedValidation(true)))
        .extract()
        .as(Order.class);
  }


  public static JsonPath getDataInStore(String endpoint, String schemePath) {
    return given()
        .filters(new AllureRestAssured())
        .relaxedHTTPSValidation()
        .log()
        .all()
        .contentType(ContentType.JSON)
        .when()
        .get(url + endpoint)
        .then()
        .log()
        .all()
        .statusCode(200)
        .statusLine("HTTP/1.1 200 OK")
        .contentType(ContentType.JSON)
        .body(JsonSchemaValidator
            .matchesJsonSchemaInClasspath(schemePath)
            .using(settings()
                .with()
                .checkedValidation(true)))
        .extract()
        .jsonPath();
  }

  public static Pet putNewData(String endpoint, String schemePath, Pet newPet) {
    return given()
        .filters(new AllureRestAssured())
        .relaxedHTTPSValidation()
        .log()
        .all()
        .contentType(ContentType.JSON)
        .body(newPet)
        .when()
        .put(url + endpoint)
        .then()
        .log()
        .all()
        .statusCode(200)
        .statusLine("HTTP/1.1 200 OK")
        .contentType(ContentType.JSON)
        .body(JsonSchemaValidator
            .matchesJsonSchemaInClasspath(schemePath)
            .using(settings()
                .with()
                .checkedValidation(true)))
        .extract()
        .response()
        .as(Pet.class);
  }


  public static JsonPath postPetNotFoundInStore(String endpoint) {
    return given()
        .filters(new AllureRestAssured())
        .relaxedHTTPSValidation()
        .log()
        .all()
        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
        .body("1")
        .when()
        .post(url + endpoint)
        .then()
        .log()
        .all()
        .statusCode(404)
        .statusLine("HTTP/1.1 404 Not Found")
        .contentType(ContentType.JSON)
        .extract()
        .jsonPath();
  }
}
