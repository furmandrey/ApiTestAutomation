package com.geekbrains.lesson3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class SpoonacularApiTaskTwo extends LogMain{

    private final String apiKey = "df2219f24e154c74a4f6fe17042f7edd";
    private final String basUrl = "https://api.spoonacular.com";
    private final String userName = "username-qwerty";
    private final String urlMealplannerAddItems = "/mealplanner/"+ userName +"/items";
    private final String hash = "f0a1f52fe8416496440363157a4d99f2ac2efc52";

    private String id;

    @Test
    void addItemToMealPlan(){
        id = given()
                .queryParam("hash", hash)
                .queryParam("apiKey", apiKey)
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
                .post(basUrl+urlMealplannerAddItems)
                .prettyPeek()
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
                .queryParam("hash", hash)
                .queryParam("apiKey", apiKey)
                .body(
                        "{\n"
                                + " \"username\":" + userName + ",\n"
                                + " \"id\":" + id + ",\n"
                                + " \"hash\":" + hash + ",\n"
                                + "}"
                )
                .delete(basUrl+urlMealplannerAddItems+"/" + id)
                .then()
                .statusCode(200);
    }






}
