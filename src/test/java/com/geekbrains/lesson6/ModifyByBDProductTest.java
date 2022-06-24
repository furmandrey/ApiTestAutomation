package com.geekbrains.lesson6;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class ModifyByBDProductTest {

    SqlSession session = null;

    long foodId;
    String titleIni = "Ale";
    String titleVice = "Lager";
    int price = 101;
    long productID;

    @BeforeEach
     void beforeEach(){
        SqlSession session = null;
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new
                    SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
            db.dao.CategoriesMapper categoriesMapper = session.getMapper(db.dao.CategoriesMapper.class);
            db.model.CategoriesExample cExample = new db.model.CategoriesExample();
            cExample.createCriteria().andTitleEqualTo("Food");
            List<db.model.Categories> list3 = categoriesMapper.selectByExample(cExample);
            db.model.Categories categories3 = list3.get(0);
            foodId = categories3.getId();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    @Test
    void modifyByProduct() throws IOException {

        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
            db.dao.ProductsMapper productsMapper = session.getMapper(db.dao.ProductsMapper.class);
            db.model.ProductsExample pExample = new db.model.ProductsExample();
            db.model.ProductsExample pExample2 = new db.model.ProductsExample();
            db.model.Products products = new db.model.Products();
            products.setTitle(titleIni);
            products.setCategory_id(foodId);
            products.setPrice(price);
            productsMapper.insert(products);
            session.commit();

            pExample.createCriteria().andTitleLike(titleIni);
            List<db.model.Products> list = productsMapper.selectByExample(pExample);
            products = list.get(0);
            assertThat(products.getTitle(), equalTo(titleIni));
            products.setTitle(titleVice);
            productsMapper.updateByPrimaryKey(products);
            pExample2.createCriteria().andTitleLike(titleVice);
            List<db.model.Products> list2 = productsMapper.selectByExample(pExample2);
            db.model.Products productsVice =list2.get(0);
            assertThat(products.getTitle(), equalTo(titleVice));
            assertThat(products.getPrice(), equalTo(price));
            assertThat(products.getCategory_id(), equalTo(foodId));
            productID = productsVice.getId();
            session.commit();

        } finally {
            session.close();
        }
    }

    @AfterEach
    void afterEach(){

        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
            db.dao.ProductsMapper productsMapper = session.getMapper(db.dao.ProductsMapper.class);
            productsMapper.deleteByPrimaryKey(productID);
            session.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
