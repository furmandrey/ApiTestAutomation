package com.geekbrains.lesson4;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ShoppingList extends LogMain{

    static RequestSpecification requestSpecification = null;
    ResponseSpecification responseSpecification = null;
    static String id1;
    private final String urlGetShoppingList = properties.getProperty("basUrl")
            + "/mealplanner/"
            + properties.getProperty("userName")
            + "/shopping-list";
    //{{baseUrl}}/mealplanner/:username/shopping-list/items/:id
    private static final String urlDelShoppingList = properties.getProperty("basUrl")
            + "/mealplanner/"
            + properties.getProperty("userName")
            + "/shopping-list/items/"
            + id1;

    @BeforeEach
    void beforeTest(){
        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", properties.getProperty("apiKey"))
                .addQueryParam("hash", properties.getProperty("hash"))
                .build();

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
            .spec(requestSpecification)
            .when()
            .get(urlGetShoppingList)
            //.prettyPeek()
            .then()
            .spec(responseSpecification);
}

    @AfterAll
    static void deleteId1() {
        given()
                .spec(requestSpecification)
                .expect()
                .body("status", equalTo("success"))
                .when().delete(urlDelShoppingList)
                //.prettyPeek()
                .then()
                .statusCode(200);
    }
    @AfterAll
    static void end() {
        System.out.println("I'l be back!");
    }

}
