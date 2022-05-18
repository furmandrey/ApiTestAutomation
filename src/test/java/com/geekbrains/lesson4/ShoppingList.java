package com.geekbrains.lesson4;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ShoppingList extends LogMain{

    //https://api.spoonacular.com/mealplanner/:username/shopping-lists
    private final String urlGetShoppingList = properties.getProperty("basUrl")
            + "/mealplanner/"
            + properties.getProperty("userName")
            + "/shopping-list";

    @BeforeEach
    void beforeTest(){
        System.out.println(urlGetShoppingList);
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(Matchers.lessThan(6000L))
                .expectHeader("Content-Type", "application/json;charset=utf-8")
                .expectHeader("Connection", "keep-alive")
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test
    void getShoppingList(){
    given()
            .queryParam("hash", properties.getProperty("hash"))
            .queryParam("apiKey", properties.getProperty("apiKey"))
            .when()
            .get(urlGetShoppingList)
            .prettyPeek()
            .then()
            .spec(responseSpecification);
}


}
