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
@SpringBootTest  // Spring Boot 애플리케이션 컨텍스트를 로드하여 통합 테스트를 수행
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)  // 테스트 메서드의 실행 순서를 지정
@ActiveProfiles("test")  // "test" 프로파일을 활성화하여 테스트 설정을 사용
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // 테스트 클래스의 생명주기를 클래스 단위로 설정





class ProductJdbcRepositoryTest {


    @Autowired
    ProductRepository repository;  // 테스트에 사용할 ProductRepository를 주입

    // 테스트에 사용할 새로운 Product 객체 생성
    private final Product newProduct = new Product(UUID.randomUUID(), "new-product", Category.COFFEE_BEAN_PACKAGE, 1000L);



    // 테스트 메서드들
    @Test
    @Order(1)
    @DisplayName("상품을 추가할 수 있다.")
    void testInsert() {
        repository.insert(newProduct);
        var all = repository.findAll();
        log.info("=============");
        log.info(all.toString());
        log.info("=============");
        assertThat(all.isEmpty(), is(false));
    }
    // ============ DONE =============================

    @Test
    @Order(2)
    @DisplayName("상품을 이름으로 조회할 수 있다.")
    void testFindByName() {
        var product = repository.findByName(newProduct.getProductName());
        log.info("=============");
        log.info(product.toString());
        log.info("=============");
        assertThat(product.isEmpty(), is(false));
    }

    @Test
    @Order(3)
    @DisplayName("상품을아이디로 조회할 수 있다.")
    void testFindById() {
        var product = repository.findById(newProduct.getProductId());
        assertThat(product.isEmpty(), is(false));
    }


    @Test
    @Order(4)
    @DisplayName("상품들을 카테고리로 조회할 수 있다.")
    void testFindByCategory() {
        var product = repository.findByCategory(Category.COFFEE_BEAN_PACKAGE);
        assertThat(product.isEmpty(), is(false));
    }

    @Test
    @Order(5)
    @DisplayName("상품을 수정할 수 있다.")
    void testUpdate() {
        newProduct.setProductName("updated-product");
        repository.update(newProduct);

        var product = repository.findById(newProduct.getProductId());
        assertThat(product.isEmpty(), is(false));
        assertThat(product.get(), samePropertyValuesAs(newProduct));
    }

    @Test
    @Order(6)
    @DisplayName("상품을 전체 삭제한다.")
    void testDeleteAll() {
        repository.deleteAll();
        var all = repository.findAll();
        assertThat(all.isEmpty(), is(true));
    }

}