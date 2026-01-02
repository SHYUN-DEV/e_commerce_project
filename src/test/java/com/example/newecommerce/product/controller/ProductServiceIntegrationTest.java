//package com.example.newecommerce.product.controller;
//
//import com.example.newecommerce.order.application.OrderService;
//import com.example.newecommerce.order.domain.Order;
//import com.example.newecommerce.order.domain.OrderRepository;
//import com.example.newecommerce.product.application.ProductService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//@SpringBootTest
//public class ProductServiceIntegrationTest {
//
//
//
//    private final ProductService productService;
//    private final OrderService orderService;
// //   private OrderRepository orderRepository;
//
//    @Autowired
//    public ProductServiceIntegrationTest(ProductService productService, OrderService orderService) {
//        this.productService = productService;
//        this.orderService = orderService;
//    }
//
//    private boolean getOrFalse(Future<Boolean> f) throws InterruptedException {
//        try {
//            return f.get(); // 정상 성공
//        } catch (ExecutionException e) {
//            return false;
//        }
//    }
//
//    @Test
//    @DisplayName("재고차감 - 비관적락")//예외가 발생 또는 실패 시나리오가 올바르게 처리가 되었는지 검증하는
//    void inventoryDeductionPessimistic() throws InterruptedException {
//
//        //아니 이게 상품 통합테스트가 맞냐
//
//
//        //주문함수 돌리고
//        Long orderId = orderService.order(userId, productIdWithQuantity);
//
//        //결제함수 돌리고
//
//
//        //주문을만 들면 주문상세 까지 만드어야
//        Order orderInfo = new Order();
//
//        List<Long> productIdList;
//
//
//
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//
//        Future<Boolean> result1 = executor.submit(() -> productService.productStockDeduct(orderInfo, productIdList));
//        Future<Boolean> result2 = executor.submit(() -> productService.productStockDeduct(orderInfo, productIdList));
//
//        boolean first = getOrFalse(result1);
//        boolean second = getOrFalse(result2);
//
//        executor.shutdown();
//
//    }
//
//
//
//}
