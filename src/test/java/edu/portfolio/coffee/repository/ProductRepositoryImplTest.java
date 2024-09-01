package edu.portfolio.coffee.repository;

import edu.portfolio.coffee.model.Category;
import edu.portfolio.coffee.model.Product;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 이 부분을 통해서 모든 테스트 코드들이 아해에 있는 스태틱 필드를 공유해서 사용한다.
class ProductJdbcRepositoryTest {
    // JUnit 5에서는 테스트 클래스의 인스턴스를 생성하고 관리하는 방법을 @TestInstance 어노테이션을 통해 제어할 수 있습니다.
    // 기본적으로 JUnit 5는 테스트 메서드를 실행할 때마다 새로운 테스트 클래스 인스턴스를 생성합니다.
    // 그러나 @TestInstance를 사용하여 인스턴스 생명 주기를 조정할 수 있습니다.




    @Autowired
    ProductRepository repository;

    //  클래스의 필드나 메서드에서 상태를 공유할 수 있습니다. newProduct와 같은 static 필드를 사용하는 것이 일반적입니다.
    private static final Product newProduct = new Product(UUID.randomUUID(), "new-product", Category.COFFEE_BEAN_PACKAGE, 1000L);

    // -> 따라서 한꺼번에 돌릴 떄 문제가 없었던 것은 위에서 테스트 코드 인스턴스 생명 주기 조정 & 필드 지정 역시 올바르게 되었다.
    // 코드에 문제가 없다는 것을 전제로 (클론 코딩)
    // @Before @After 로 적용시킨, 내장 임베디드 mySql 이 아닌 실 디비를 연결한 점이 달라졌고
    @Test
    @Order(1)
    @DisplayName("상품을 추가할 수 있다.")
    void testInsert() {
        log.info("### 1. Inserting product: {}", newProduct.toString());
        repository.insert(newProduct);

        var all = repository.findAll();
        log.info("Retrieved all products: {}", all);
        assertThat(all.isEmpty(), is(false));
    }

    @Test
    @Order(2)
    @DisplayName("상품을 이름으로 조회할 수 있다.")
    void testFindByName() {
        log.info("### 2. Searching for product by name: {}", newProduct.getProductName());
        var product = repository.findByName(newProduct.getProductName());

        log.info("Retrieved product: {}", product);
        assertThat(product.isEmpty(), is(false));
    }

    @Test
    @Order(3)
    @DisplayName("상품을 아이디로 조회할 수 있다.")
    void testFindById() {
        log.info("### 3. Searching for product by ID: {}", newProduct.getProductId());
        var product = repository.findById(newProduct.getProductId());

        log.info("Retrieved product: {}", product);
        assertThat(product.isEmpty(), is(false));
    }

    @Test
    @Order(4)
    @DisplayName("상품들을 카테고리로 조회할 수 있다.")
    void testFindByCategory() {
        log.info("### 4. Searching for products by category: {}", Category.COFFEE_BEAN_PACKAGE);
        var products = repository.findByCategory(Category.COFFEE_BEAN_PACKAGE);

        log.info("Retrieved products: {}", products);
        assertThat(products.isEmpty(), is(false));
    }

    @Test
    @Order(5)
    @DisplayName("상품을 수정할 수 있다.")
    void testUpdate() {
        log.info("### 5. Updating product: {}", newProduct);
        newProduct.setProductName("updated-product");
        repository.update(newProduct);

        var product = repository.findById(newProduct.getProductId());
        log.info("Retrieved updated product: {}", product);
        assertThat(product.isEmpty(), is(false));
        assertThat(product.get(), samePropertyValuesAs(newProduct));
    }

    @Test
    @Order(6)
    @DisplayName("상품을 전체 삭제한다.")
    void testDeleteAll() {
        log.info("### 6. Deleting all products.");
        repository.deleteAll();

        var all = repository.findAll();
        log.info("Retrieved all products after delete: {}", all);
        assertThat(all.isEmpty(), is(true));
    }


}