package edu.portfolio.coffee.repository;

// Order 및 OrderItem 모델 클래스와 NamedParameterJdbcTemplate 관련 라이브러리 임포트
import edu.portfolio.coffee.model.Order;
import edu.portfolio.coffee.model.OrderItem;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository  // 해당 클래스가 데이터베이스와의 상호작용을 담당하는 리포지토리임을 명시
public class OrderRepositoryImpl implements OrderRepository {

    // NamedParameterJdbcTemplate를 통한 의존성 주입
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 트랜잭션 처리를 통해 주문 데이터를 데이터베이스에 삽입
    @Transactional
    @Override
    public Order insert(Order order) {
        // 주문 데이터를 'orders' 테이블에 삽입
        jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) " +
                        "VALUES (UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)",
                toOrderParamMap(order));

        // 각 주문 항목(OrderItem)을 'order_items' 테이블에 삽입
        order.getOrderItems()
                .forEach(item ->
                        jdbcTemplate.update("INSERT INTO order_items(order_id, product_id, category, price, quantity, created_at, updated_at) " +
                                        "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :category, :price, :quantity, :createdAt, :updatedAt)",
                                toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(), order.getUpdatedAt(), item)));
        return order;
    }

    // 주문 데이터를 SQL 쿼리에 맞는 파라미터 맵으로 변환하는 메서드
    private Map<String, Object> toOrderParamMap(Order order) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", order.getOrderId().toString().getBytes()); // 주문 ID를 바이트 배열로 변환
        paramMap.put("email", order.getEmail().getAddress()); // 이메일 주소
        paramMap.put("address", order.getAddress()); // 배송 주소
        paramMap.put("postcode", order.getPostcode()); // 우편번호
        paramMap.put("orderStatus", order.getOrderStatus().toString()); // 주문 상태
        paramMap.put("createdAt", order.getCreatedAt()); // 주문 생성 시간
        paramMap.put("updatedAt", order.getUpdatedAt()); // 주문 업데이트 시간
        return paramMap;
    }

    // 주문 항목 데이터를 SQL 쿼리에 맞는 파라미터 맵으로 변환하는 메서드
    private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt, LocalDateTime updatedAt, OrderItem item) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId.toString().getBytes()); // 주문 ID를 바이트 배열로 변환
        paramMap.put("productId", item.productId().toString().getBytes()); // 제품 ID를 바이트 배열로 변환
        paramMap.put("category", item.category().toString()); // 제품 카테고리
        paramMap.put("price", item.price()); // 제품 가격
        paramMap.put("quantity", item.quantity()); // 주문 수량
        paramMap.put("createdAt", createdAt); // 생성 시간
        paramMap.put("updatedAt", updatedAt); // 업데이트 시간
        return paramMap;
    }
}