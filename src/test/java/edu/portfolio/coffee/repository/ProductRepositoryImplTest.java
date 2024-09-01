package edu.portfolio.coffee.repository;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.config.Charset;
import edu.portfolio.coffee.model.Category;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import edu.portfolio.coffee.model.Product;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


@SpringBootTest  // Spring Boot 애플리케이션 컨텍스트를 로드하여 통합 테스트를 수행
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)  // 테스트 메서드의 실행 순서를 지정
@ActiveProfiles("test")  // "test" 프로파일을 활성화하여 테스트 설정을 사용
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // 테스트 클래스의 생명주기를 클래스 단위로 설정
class ProductJdbcRepositoryTest {

    // 정적 필드로 EmbeddedMysql 인스턴스를 선언
    static EmbeddedMysql embeddedMysql;

    @BeforeAll
    static void setup() {
        // MySQL 서버를 구성
        var config = aMysqldConfig(v8_0_11)  // MySQL 8.0.11 버전 사용
                .withCharset(Charset.UTF8)  // UTF-8 문자셋 설정
                .withPort(2215)  // MySQL 서버가 사용할 포트 설정
                .withUser("test", "test1234!")  // 사용자 이름 및 비밀번호 설정
                .withTimeZone("Asia/Seoul")  // 타임존 설정
                .build();

        // EmbeddedMysql 인스턴스를 생성하고 구성 설정을 적용
        embeddedMysql = anEmbeddedMysql(config)
                .addSchema("test-order_mgmt", ScriptResolver.classPathScript("schema.sql"))  // 데이터베이스 스키마를 스크립트 파일로부터 로드
                .start();  // MySQL 서버 시작
    }

    @AfterAll
    static void cleanup() {
        // 테스트 종료 후 EmbeddedMysql 서버를 정지
        embeddedMysql.stop();
    }

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
        assertThat(all.isEmpty(), is(false));
    }

    @Test
    @Order(2)
    @DisplayName("상품을 이름으로 조회할 수 있다.")
    void testFindByName() {
        var product = repository.findByName(newProduct.getProductName());
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