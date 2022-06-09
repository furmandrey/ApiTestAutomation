package com.geekbrains.lesson5;

import com.geekbrains.lesson5.api.ProductService;
import com.geekbrains.lesson5.dto.Product;
import com.geekbrains.lesson5.utils.RetrofitUtils;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetProductsTest {
    static ProductService productService;
    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @SneakyThrows
    @Test
    void getProductsTest() {
        Response<ResponseBody> response = productService.getProducts().execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));


    }

    @SneakyThrows
    @Test
    void getProductsByIdTest() {
        Response<Product> response = productService.getProductById(1).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(1));
        assertThat(response.body().getPrice(), equalTo(95));
        assertThat(response.body().getTitle(), equalTo("Milk"));
        assertThat(response.body().getCategoryTitle(), equalTo("Food"));


    }
}
