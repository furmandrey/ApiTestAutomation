package com.geekbrains.lesson6;

import com.geekbrains.lesson5.api.ProductService;
import com.geekbrains.lesson5.dto.Product;
import com.geekbrains.lesson5.utils.RetrofitUtils;
import com.github.javafaker.Faker;
import db.model.Products;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ModifyProductTest {

    static ProductService productService;
    Product productInit = null;
    Product productVice = null;
    Faker faker = new Faker();
    static int id;
    String title = "Ale";
    int price = 100;
    String categoryTitle = "Food";

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @SneakyThrows
    @BeforeEach
    void setUp() {
        productInit = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 1000));

        Response<Product> response = productService.createProduct(productInit)
                .execute();
        id =  response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        productVice = new Product()
                .withId(id)
                .withTitle(title)
                .withPrice(price)
                .withCategoryTitle(categoryTitle);
    }
    @SneakyThrows
    @Test
    void modifyProductTest() {
        Response<Product> response = productService.modifyProduct(productVice).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(id));
        assertThat(response.body().getPrice(), equalTo(price));
        assertThat(response.body().getTitle(), equalTo(title));
        assertThat(response.body().getCategoryTitle(), equalTo(categoryTitle));

        SqlSession session = null;
        Products products;
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
            db.dao.ProductsMapper productsMapper = session.getMapper(db.dao.ProductsMapper.class);
            db.model.ProductsExample example = new db.model.ProductsExample();
            example.createCriteria().andIdEqualTo((long) id);
            List<db.model.Products> list = productsMapper.selectByExample(example);
            assertThat(productsMapper.countByExample(example), equalTo(1L));
            products = list.get(0);

        } finally {
            session.close();
        }
        assertThat(products.getTitle(), equalTo(title));
        assertThat(products.getId(), equalTo(id));
        assertThat(products.getPrice(), equalTo(price));
    }

    @SneakyThrows
    @AfterAll
    static void tearDown() {
        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

        SqlSession session = null;
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
            db.dao.ProductsMapper productsMapper = session.getMapper(db.dao.ProductsMapper.class);
            db.model.ProductsExample example = new db.model.ProductsExample();
            example.createCriteria().andIdEqualTo((long) id);
            List<db.model.Products> list = productsMapper.selectByExample(example);
            assertThat(productsMapper.countByExample(example), equalTo(0L));


        } finally {
            session.close();
        }
    }



}
