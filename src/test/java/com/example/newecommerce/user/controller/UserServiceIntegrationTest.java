package com.example.newecommerce.user.controller;


import com.example.newecommerce.order.application.OrderService;

import com.example.newecommerce.product.domain.ProductRepository;
import com.example.newecommerce.user.application.UserServiceImpl;

import com.example.newecommerce.user.domain.User;
import com.example.newecommerce.user.domain.UserRepository;
import com.example.newecommerce.user.dto.PointResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserServiceIntegrationTest {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserServiceImpl userService;
    private final OrderService orderService;

//    @Autowired
//    private UserServiceImpl userServiceImpl;
//    @Autowired
//    private OrderRepository orderRepository;


    @Autowired
    public UserServiceIntegrationTest(UserRepository userRepository,
                                      ProductRepository productRepository,
                                      UserServiceImpl userService,
                                      OrderService orderService) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.userService = userService;
        this.orderService = orderService;
    }


    private PointResponse getOrNull(Future<PointResponse> f) throws InterruptedException {
        try {
            return f.get(); // 정상 성공
        } catch (ExecutionException e) {
            return null;
        }
    }
    private boolean getOrFalse(Future<Boolean> f) throws InterruptedException {
        try {
            return f.get(); // 정상 성공
        } catch (ExecutionException e) {
            return false;
        }
    }



    @Test
    @DisplayName("포인트 충전 - 중복요청")
    void chargeTestFailDuplicationRequest() throws ExecutionException, InterruptedException {


        long userId = 1;
        int point = 1000;

        User beforUpdateUser = userRepository.findByUserId(userId);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<PointResponse> result1 = executor.submit(() -> userService.chargePoint(userId, point));
        Future<PointResponse> result2 = executor.submit(() -> userService.chargePoint(userId, point));

        PointResponse first = getOrNull(result1);
        PointResponse second = getOrNull(result2);

        executor.shutdown();


        // 최종 포인트 확인
        User afterUpdateUser = userRepository.findByUserId(userId);

        assertEquals(afterUpdateUser.getPoint().getPoint(), first.getPoint());
        assertEquals(null, second);



    }




    @Test
    @DisplayName("포인트 사용 - 중복요청")  //동시성이 처리가 안되어있는 상황을 올바르게처리하는
    void useTestFailDuplicationRequest() throws ExecutionException, InterruptedException {


//        long userId = 3;
//        int point = 1000;
//
//        User beforUpdateUser = userRepository.findByUserId(userId);
//
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//
//        Future<Boolean> result1 = executor.submit(() -> userService.usePoint(userId, point));
//        Future<Boolean> result2 = executor.submit(() -> userService.usePoint(userId, point));
//
//        boolean first = getOrFalse(result1);
//        boolean second = getOrFalse(result2);
//        System.out.println("((((((((((((((((((((((((((((((((" + result1);
//        System.out.println("((((((((((((((((((((((((((((((((" + result2);
//
//        executor.shutdown();
//
//
//        assertTrue(first || second);
//        assertFalse(first && second);
//
//
//
//
//        // 최종 포인트 확인
//        User afterUpdateUser = userRepository.findByUserId(userId);
//
//        int expected = beforUpdateUser.getPoint().getPoint() - (first ? point : 0) - (second ? point : 0);
//        assertEquals(expected, afterUpdateUser.getPoint().getPoint());








//        //유저 생성
//        //long userId = 99L;
//        int p1 = 1000; int p2 = 2000; int p3 = 3000;
//        int totalPayPoint = p1 + p2 + p3;
//
//
//        Point pt = new Point();
//        pt.setPoint(totalPayPoint);
//
//        User testUser = new User();
//        //testUser.setUserId(99L);
//        testUser.setUserName("testUser");
//        testUser.setPoint(pt);
//
//        pt.setUser(testUser);
//
//        userRepository.save(testUser);
//        Long userId = testUser.getUserId();
//
//
//
//        Product prod1 = new Product();
//        prod1.setProcuctName("테스트상품1");
//        prod1.setPruductPrice(p1);
//        prod1.setInventory(100);
//
//        Product prod2 = new Product();
//        prod2.setProcuctName("테스트상품2");
//        prod2.setPruductPrice(p2);
//        prod2.setInventory(100);
//
//        Product prod3 = new Product();
//        prod3.setProcuctName("테스트상품3");
//        prod3.setPruductPrice(p3);
//        prod3.setInventory(100);
//
//        productRepository.save(prod1);
//        productRepository.save(prod2);
//        productRepository.save(prod3);
//
//        //주문생성
//        Map<Long, Integer> productIdWithQty = new HashMap<>();
//        productIdWithQty.put(prod1.getProductId(), 1);
//        productIdWithQty.put(prod2.getProductId(), 1);
//        productIdWithQty.put(prod3.getProductId(), 1);
//
//        Long orderId = orderService.order(testUser.getUserId(), productIdWithQty);
//
//
//
//
//
//
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//
//        Future<Boolean> result1 = executor.submit(() -> orderService.paymentPoint(userId, orderId, totalPayPoint));
//        Future<Boolean> result2 = executor.submit(() -> orderService.paymentPoint(userId, orderId, totalPayPoint));
//
//        boolean first = result1.get();
//        boolean second = result2.get();
//
//        executor.shutdown();
//
//
//        assertTrue(first || second);
//        assertFalse(second);



    }






}
