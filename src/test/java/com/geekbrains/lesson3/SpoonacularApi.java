package com.geekbrains.lesson3;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;


public class SpoonacularApi extends LogMain {

    private final String apiKey = "df2219f24e154c74a4f6fe17042f7edd";
    private final String basUrlComplexSearch = "https://api.spoonacular.com/recipes/complexSearch";

    @Test
    void getVegetarianBurger() {
        given()

                .queryParam("apiKey", apiKey)
                .queryParam("query", "burger")
                .queryParam("diet", "vegetarian")
                .expect()
                .body("totalResults", equalTo(3) )
                .body("results[0].title", equalTo("Falafel Burger"))
                .body("results[0].title", either(containsString("Falafel Burger"))
                        .or(containsString("Butternut Squash Quinoa Burgers"))
                        .or(containsString("Walnut Lentil Burgers with Tarragon")))

                .when()
                .get(basUrlComplexSearch)
                //.get("https://api.spoonacular.com/recipes/complexSearch?apiKey=df2219f24e154c74a4f6fe17042f7edd&query=burger&diet=vegetarian")
                .then()
                .statusCode(200);


    }

    @AfterAll
    static void end() {
        System.out.println("I'l be back!");
    }

}
