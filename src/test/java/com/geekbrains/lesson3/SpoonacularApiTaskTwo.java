package com.geekbrains.lesson3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class SpoonacularApiTaskTwo extends LogMain{

    private final String urlMealplannerAddItems = "/mealplanner/"+ (properties.getProperty("userName")) + "/items";
    private String id;

    @Test
    void addItemToMealPlan(){
        id = given()
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
                //.prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();
    }
    @AfterEach
    void tearDown() {
        given()
                .queryParam("hash", properties.getProperty("hash"))
                .queryParam("apiKey", properties.getProperty("apiKey"))
                .body(
                        "{\n"
                                + " \"username\":" + (properties.getProperty("userName")) + ",\n"
                                + " \"id\":" + id + ",\n"
                                + " \"hash\":" + properties.getProperty("hash") + ",\n"
                                + "}"
                )
                .delete(properties.getProperty("basUrl") + urlMealplannerAddItems + "/" + id)
                .then()
                .statusCode(200);
    }

}
