package com.prgrms.repository;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.prgrms.model.Category;
import com.prgrms.model.Product;
import com.wix.mysql.EmbeddedMysql;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@Import(JdbcBase.class)
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ProductRepositoryImplTest {

    @Autowired
    private ProductRepository repository;

    private static EmbeddedMysql embeddedMysql;

    private final Product newProduct = new Product(UUID.randomUUID(), "new-product",
        Category.COFFEE_BEAN_COFFEE, 1000L);


    @BeforeAll
    static void setup() {
        var mysqlConfig = aMysqldConfig(v8_0_11)
            .withCharset(UTF8)
            .withPort(10000)
            .withUser("test", "test1234!")
            .withTimeZone("Asia/Seoul")
            .build();

        embeddedMysql = anEmbeddedMysql(mysqlConfig)
            .addSchema("coffee", classPathScript("db/mysql/init/schema.sql"))
            .start();
    }

    @AfterAll
    static void cleanup() {
        embeddedMysql.stop();
    }


    @Test
    @Order(1)
    @DisplayName("상품을 추가할 수 있다.")
    void test() {
        repository.insert(newProduct);
        var all = repository.findAll();
        assertFalse(all.isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("상품을 이름으로 조회할 수 있다.")
    void testFindByName() {
        Optional<Product> product = repository.findByName(newProduct.getProductName());
        assertFalse(product.isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("상품을 아이디로 조회할 수 있다.")
    void testFindById() {
        Optional<Product> product = repository.findById(newProduct.getProductId());
        assertFalse(product.isEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("상품을 카테고리로 조회할 수 있다.")
    void testFindByCategory() {
        List<Product> product = repository.findByCategory(Category.COFFEE_BEAN_COFFEE);
        assertFalse(product.isEmpty());
    }

    @Test
    @Order(5)
    @DisplayName("상품을 수정할 수 있다")
    void testUpdate() {
        newProduct.setProductName("updated-product");
        repository.update(newProduct);

        Optional<Product> product = repository.findById(newProduct.getProductId());
        assertEquals(product.get().getProductName(), "updated-product");
    }

    @Test
    @Order(6)
    @DisplayName("상품을 전체 삭제한다")
    void testDeleteAll() {
        repository.deleteAll();
        List<Product> productList = repository.findAll();
        assertTrue(productList.isEmpty());
    }

}