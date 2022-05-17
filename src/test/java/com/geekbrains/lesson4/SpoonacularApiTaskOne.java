package com.geekbrains.lesson4;

import org.hamcrest.Matchers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;


public class SpoonacularApiTaskOne extends LogMain {

    private final String apiKey = "df2219f24e154c74a4f6fe17042f7edd";
    private final String basUrl = "https://api.spoonacular.com";
    private final String urlComplexSearch = "/recipes/complexSearch";
    private final String urlrecipesCuisine = "/recipes/cuisine";

    @Test
    void getVegetarianBurger() {
        String burger1 = "Falafel Burger";
        String burger2 = "Butternut Squash Quinoa Burgers";
        String burger3 = "Walnut Lentil Burgers with Tarragon";
        given()

                .queryParam("apiKey", apiKey)
                .queryParam("query", "burger")
                .queryParam("diet", "vegetarian")
                .expect()
                .body("totalResults", equalTo(3))
                //.body("results[0].title", equalTo("Falafel Burger"))
                .body("results[0].title", either(containsString(burger1))
                        .or(containsString(burger2)).or(containsString(burger3)))
                .body("results[1].title", either(containsString(burger1)).or(containsString(burger2))
                        .or(containsString(burger3)))
                .body("results[2].title", either(containsString(burger1))
                        .or(containsString(burger2)).or(containsString(burger3)))
                .header("Connection", "keep-alive")
                .when()
                .get(basUrl+urlComplexSearch)
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(6000L));

    }

    @Test
    void getGlutenFreeBurger() {
        String burger1 = "Turkey Burgers with Slaw";
        String burger2 = "Walnut Lentil Burgers with Tarragon";
        String burger3 = "Mini Zucchini Avocado Burgers";
        given()

                .queryParam("apiKey", apiKey)
                .queryParam("query", "burger")
                .queryParam("diet", "Gluten Free")
                .expect()
                .body("totalResults", equalTo(3))
                .body("results[0].title", either(containsString(burger1))
                        .or(containsString(burger2)).or(containsString(burger3)))
                .body("results[1].title", either(containsString(burger1)).or(containsString(burger2))
                        .or(containsString(burger3)))
                .body("results[2].title", either(containsString(burger1))
                        .or(containsString(burger2)).or(containsString(burger3)))
                .header("Connection", "keep-alive")
                .when()
                .get(basUrl+urlComplexSearch)
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(6000L));
    }

    @Test
    void getNegativeIfVegetarianContainsFish() {
        given()

                .queryParam("apiKey", apiKey)
                .queryParam("includeIngredients", "fish")
                .queryParam("diet", "vegetarian")
                .expect()
                .body("totalResults", equalTo(0))
                .body("offset", equalTo(0))
                .body("results", hasSize(0))
                .header("Connection", "keep-alive")
                .when()
                .get(basUrl+urlComplexSearch)
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(6000L));
    }

    @Test
    void getDrinkMilkWormwoodPepper() {

        String drink1 = "Cookinghow Penne Alla Vodka";
        String drink2 = "Spiced Lassi";

        given()
                //.log()
                //.all()
                .queryParam("apiKey", apiKey)
                .queryParam("type", "drink")
                .queryParam("includeIngredients", "milk, wormwood, pepper")
                .expect()
                .body("totalResults", equalTo(2))
                .body("results[0].title", either(containsString(drink1))
                        .or(containsString(drink2)))
                .body("results[1].title", either(containsString(drink1))
                        .or(containsString(drink2)))
                .header("Connection", "keep-alive")
                .when()
                .get(basUrl+urlComplexSearch)
                //.prettyPeek()
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(6000L));
    }

    @Test
    void getVegetarianWithMinProtein50FillIngredients() {
        given()

                .queryParam("apiKey", apiKey)
                .queryParam("minProtein", 51)
                .queryParam("diet", "vegetarian")
                .queryParam("fillIngredients", "true")
                .expect()
                .body("totalResults", equalTo(1))
                .body("results[0].title", equalTo("Chia Yogurt Apricot Bowl"))
                .body("results[0].missedIngredients[0].amount", equalTo(0.25F))
                .header("Connection", "keep-alive")
                .when()
                .get(basUrl+urlComplexSearch)
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(6000L));
    }

    @Test
    void postFalafelBurger(){
        given()
                .queryParam("apiKey", apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("title", "Falafel Burger")
                .expect()
                .body("cuisine", equalTo("Middle Eastern"))
                .body("confidence", equalTo(0.85F))
                .header("Content-Type", "application/json")
                .when()
                .post(basUrl+urlrecipesCuisine)
                //.prettyPeek()
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(6000L));

    }

    @Test
    void postThaiPastaSalad(){
        String cuisine1 = "Asian";
        String cuisine2 = "Thai";

        given()
                .queryParam("apiKey", apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("title", "Thai Pasta Salad")
                .expect()
                .body( "cuisines", hasItems(cuisine1, cuisine2 ))
                .body("confidence", equalTo(0.85F))
                .header("Content-Type", "application/json")
                .when()
                .post(basUrl+urlrecipesCuisine)
                //.prettyPeek()
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(6000L));

    }

    @Test
    void postJensSwedishMeatballs(){
        String cuisine1 = "Scandinavian";
        String cuisine2 = "European";
        String cuisine3 = "Nordic";

        given()
                .queryParam("apiKey", apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("title", "Jen's Swedish Meatballs")
                .expect()
                .body( "cuisines", hasItems(cuisine1, cuisine2, cuisine3 ))
                .body("confidence", equalTo(0.85F))
                //.body("results[0].missedIngredients[0].amount", equalTo(0.25F))
                .header("Content-Type", "application/json")
                .when()
                .post(basUrl+urlrecipesCuisine)
                //.prettyPeek()
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(6000L));
    }

    @Test
    void postMangoFriedRice(){
        String cuisine1 = "Asian";
        String cuisine2 = "Chinese";

        given()
                .queryParam("apiKey", apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("title", "Mango Fried Rice")
                .expect()
                .body( "cuisines", hasItems(cuisine1, cuisine2 ))
                .body("confidence", equalTo(0.85F))
                .header("Content-Type", "application/json")
                .when()
                .post(basUrl+urlrecipesCuisine)
                //.prettyPeek()
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(6000L));
    }

    @Test
    void postAfricanChickenPeanutStew(){
        given()
                .queryParam("apiKey", apiKey)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("title", "African Chicken Peanut Stew")
                .expect()
                .body("cuisine", equalTo("African"))
                .body("confidence", equalTo(0.85F))
                .header("Content-Type", "application/json")
                .when()
                .post(basUrl+urlrecipesCuisine)
                //.prettyPeek()
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(6000L));

    }

    @AfterAll
    static void end() {
        System.out.println("I'l be back!");
    }

}
