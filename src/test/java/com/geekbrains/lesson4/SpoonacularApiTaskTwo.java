package com.geekbrains.lesson4;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class SpoonacularApiTaskTwo extends LogMain{

    private static final String urlMealplannerAddItems = "/mealplanner/"+ (properties.getProperty("userName")) + "/items";
    private static String id1;
    private static String id2;

    @Test
    void addItem1ToMealPlan(){
        id1 = given()
                .queryParam("hash", properties.getProperty("hash"))
                .queryParam("apiKey", properties.getProperty("apiKey"))
                .body("{\n"
                        + " \"date\": 20220514,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"1 banana\",\n"
                        + " \"name\": \"1 potato\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post(properties.getProperty("basUrl") + urlMealplannerAddItems)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();
    }

    @Test
    void addItem2ToMealPlan(){
        id2 = given()
                .queryParam("hash", properties.getProperty("hash"))
                .queryParam("apiKey", properties.getProperty("apiKey"))
                .body("{\n"
                        + " \"date\": 20220514,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"1 potato\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post(properties.getProperty("basUrl") + urlMealplannerAddItems)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();
    }

    @AfterAll
    static void tearDown1() {
        given()
                .queryParam("hash", properties.getProperty("hash"))
                .queryParam("apiKey", properties.getProperty("apiKey"))
                .body(
                        "{\n"
                                + " \"username\":" + (properties.getProperty("userName")) + ",\n"
                                + " \"id\":" + id1 + ",\n"
                                + " \"hash\":" + properties.getProperty("hash") + ",\n"
                                + "}"
                )
                .delete(properties.getProperty("basUrl") + urlMealplannerAddItems + "/" + id1)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }
    @AfterAll
    static void tearDown2() {
        given()
                .queryParam("hash", properties.getProperty("hash"))
                .queryParam("apiKey", properties.getProperty("apiKey"))
                .body(
                        "{\n"
                                + " \"username\":" + (properties.getProperty("userName")) + ",\n"
                                + " \"id\":" + id2 + ",\n"
                                + " \"hash\":" + properties.getProperty("hash") + ",\n"
                                + "}"
                )
                .delete(properties.getProperty("basUrl") + urlMealplannerAddItems + "/" + id2)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

}
