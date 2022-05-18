package com.geekbrains.lesson4;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;


public class ComplexSearchAndCuisine extends LogMain {

    private final String apiKey = "df2219f24e154c74a4f6fe17042f7edd";
    private final String basUrl = "https://api.spoonacular.com";
    private final String urlComplexSearch = "/recipes/complexSearch";
    private final String urlrecipesCuisine = "/recipes/cuisine";

    private static final String urlMealplannerAddItems = "/mealplanner/"+ (properties.getProperty("userName")) + "/items";
    private static String id1;
    private static String id2;


    @BeforeEach
    void beforeTest(){
        requestSpecificationGet = new RequestSpecBuilder()
                .addQueryParam("apiKey", apiKey)
                .build();

        requestSpecificationPost = new RequestSpecBuilder()
                .addQueryParam("apiKey", apiKey)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(Matchers.lessThan(6000L))
                .expectHeader("Content-Type", "application/json")
                .expectHeader("Connection", "keep-alive")
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test
    void getVegetarianBurger() {
        String burger1 = "Falafel Burger";
        String burger2 = "Butternut Squash Quinoa Burgers";
        String burger3 = "Walnut Lentil Burgers with Tarragon";
        given()

                .spec(requestSpecificationGet)
                .queryParam("query", "burger")
                .queryParam("diet", "vegetarian")
                .expect()
                .body("totalResults", equalTo(3))
                .body("results[0].title", either(containsString(burger1))
                        .or(containsString(burger2)).or(containsString(burger3)))
                .body("results[1].title", either(containsString(burger1)).or(containsString(burger2))
                        .or(containsString(burger3)))
                .body("results[2].title", either(containsString(burger1))
                        .or(containsString(burger2)).or(containsString(burger3)))
                //.header("Connection", "keep-alive")
                .when()
                .get(basUrl+urlComplexSearch)
                .then()
                .spec(responseSpecification);


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
                //.prettyPeek()
                .then()
                .spec(responseSpecification);
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
                .spec(responseSpecification);
    }

    @Test
    void getDrinkMilkWormwood() {

        //String drink1 = "Cookinghow Penne Alla Vodka";
        //String drink2 = "Spiced Lassi";

        given()
                //.log()
                //.all()
                .queryParam("apiKey", apiKey)
                .queryParam("type", "drink")
                .queryParam("includeIngredients", "milk, wormwood")
                .expect()
                .body("totalResults", equalTo(1))
                .body("results[0].title", equalTo("Milky Watermelon Drink"))
//                .body("results[0].title", either(containsString(drink1))
//                        .or(containsString(drink2)))
//                .body("results[1].title", either(containsString(drink1))
//                        .or(containsString(drink2)))
                .header("Connection", "keep-alive")
                .when()
                .get(basUrl+urlComplexSearch)
                //.prettyPeek()
                .then()
                .spec(responseSpecification);
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
                .spec(responseSpecification);
    }

    @Test
    void postFalafelBurger(){
        given()
                .spec(requestSpecificationPost)
                .formParam("title", "Falafel Burger")
                .expect()
                .body("cuisine", equalTo("Middle Eastern"))
                .body("confidence", equalTo(0.85F))
                .header("Content-Type", "application/json")
                .when()
                .post(basUrl+urlrecipesCuisine)
                //.prettyPeek()
                .then()
                .spec(responseSpecification);

    }

    @Test
    void postThaiPastaSalad(){
        String cuisine1 = "Asian";
        String cuisine2 = "Thai";

        given()
                .spec(requestSpecificationPost)
                .formParam("title", "Thai Pasta Salad")
                .expect()
                .body( "cuisines", hasItems(cuisine1, cuisine2 ))
                .body("confidence", equalTo(0.85F))
                .header("Content-Type", "application/json")
                .when()
                .post(basUrl+urlrecipesCuisine)
                //.prettyPeek()
                .then()
                .spec(responseSpecification);

    }

    @Test
    void postJensSwedishMeatballs(){
        String cuisine1 = "Scandinavian";
        String cuisine2 = "European";
        String cuisine3 = "Nordic";

        given()
                .spec(requestSpecificationPost)
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
                .spec(responseSpecification);
    }

    @Test
    void postMangoFriedRice(){
        String cuisine1 = "Asian";
        String cuisine2 = "Chinese";

        given()
                .spec(requestSpecificationPost)
                .formParam("title", "Mango Fried Rice")
                .expect()
                .body( "cuisines", hasItems(cuisine1, cuisine2 ))
                .body("confidence", equalTo(0.85F))
                .header("Content-Type", "application/json")
                .when()
                .post(basUrl+urlrecipesCuisine)
                //.prettyPeek()
                .then()
                .spec(responseSpecification);
    }

    @Test
    void postAfricanChickenPeanutStew(){
        given()
                .spec(requestSpecificationPost)
                .formParam("title", "African Chicken Peanut Stew")
                .expect()
                .body("cuisine", equalTo("African"))
                .body("confidence", equalTo(0.85F))
                .header("Content-Type", "application/json")
                .when()
                .post(basUrl+urlrecipesCuisine)
                //.prettyPeek()
                .then()
                .spec(responseSpecification);

    }
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
    @AfterAll
    static void end() {
        System.out.println("I'l be back!");
    }

}
