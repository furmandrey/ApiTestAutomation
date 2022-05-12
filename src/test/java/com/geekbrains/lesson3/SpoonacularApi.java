package com.geekbrains.lesson3;

import org.hamcrest.Matchers;
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
        String burger1 = "Falafel Burger";
        String burger2 = "Butternut Squash Quinoa Burgers";
        String burger3 = "Walnut Lentil Burgers with Tarragon";
        given()

                .queryParam("apiKey", apiKey).queryParam("query", "burger")
                .queryParam("diet", "vegetarian")
                .expect().body("totalResults", equalTo(3))
                .body("results[0].title", equalTo("Falafel Burger"))
                .body("results[0].title", either(containsString(burger1))
                        .or(containsString(burger2)).or(containsString(burger3)))
                .body("results[1].title", either(containsString(burger1)).or(containsString(burger2))
                        .or(containsString(burger3)))
                .body("results[2].title", either(containsString(burger1))
                        .or(containsString(burger2)).or(containsString(burger3)))
                .header("Connection", "keep-alive")
                .when()
                .get(basUrlComplexSearch)
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(6000L));

    }

    @Test
    void get2() {

    }

    @Test
    void get3() {

    }

    @Test
    void get4() {

    }

    @Test
    void get5() {

    }

    @AfterAll
    static void end() {
        System.out.println("I'l be back!");
    }

}
