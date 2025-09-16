package com.example.newecommerce.product.controller;

import com.example.newecommerce.common.exception.BusinessException;
import com.example.newecommerce.product.application.ProductServiceImpl;

import com.example.newecommerce.product.domain.ProductRepository;
import com.example.newecommerce.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    /*
    실패 테스트 코드 - 예외 상황이아 실패상황이 올르바게 처리 되는 확인 하는 테스트 코드

    상품조회
    실패 - 상품정보 없음

    베스트상품조회
    실패 - 상품정보없음

    */


    @Mock
    private ProductRepository productRepository;

//    @Mock
//    private UserRepository userRepository;

    @InjectMocks
    private ProductServiceImpl productService;



    @DisplayName("상품조회 - 실패 - 상품정보 없음")
    @Test
    void productInquiryFailNoProduct() {

        //given
        List<Long> productIdList = new ArrayList<>();
        productIdList.add(1L);
        productIdList.add(2L);
        productIdList.add(3L);


        //when
        when(productRepository.findAllById(productIdList)).thenReturn(new ArrayList<>());


       // List<ProductResponse> productList = productService.inquiryProduct(productIdList);

        //then
        assertThrows(BusinessException.class,
                () -> productService.inquiryProduct(productIdList));
        verify(productRepository, times(1)).findAllById(any());




    }
    @DisplayName("베스트상품조회 - 실패 - 상품정보없음")
    @Test
    void bestProductInquireFailDuplicateProduct() {

        //given
        when(productRepository.bestProductInquiry()).thenReturn(null);

        //when
       // productService.inquiryBestProduct();

        //then
        assertThrows(BusinessException.class,
                () -> productService.inquiryBestProduct());
        verify(productRepository, times(1)).bestProductInquiry();



    }

//    @DisplayName("주문 - 실패 - 계정정보없음")
//    @Test
//    void orderFailNoMember() {
//
//        //given
//        Long userId = 99L;
//        HashMap<Long, Integer> productIdWithQuantity = new HashMap();
//        productIdWithQuantity.put(1L, 5);
//        productIdWithQuantity.put(2L, 7);
//        productIdWithQuantity.put(3L, 15);
//
//        when(userRepository.findByUserId(userId)).thenReturn(null);
//
//        //when
//       // productService.order(userId, productIdWithQuantity);
//
//        //then
//
//        assertThrows(BusinessException.class,
//                () -> productService.order(userId, productIdWithQuantity));
//        verify(productRepository, never()).save(any());
//
//
//    }
//
//    @DisplayName("주문 - 실패 - 상품정보없음")
//    @Test
//    void orderFailNoProduct() {
//
//        //given
//        Long userId = 99L;
//        User user = new User(99L, "테스트유저", new Point(500000));
//
//        List<Long> productIdList = new ArrayList<>();
//        productIdList.add(1L);
//        productIdList.add(2L);
//        productIdList.add(3L);
//
//        HashMap<Long, Integer> productIdWithQuantity = new HashMap();
//        productIdWithQuantity.put(1L, 5);
//        productIdWithQuantity.put(2L, 7);
//        productIdWithQuantity.put(3L, 15);
//
//        when(userRepository.findByUserId(userId)).thenReturn(user);
//        when(productRepository.findAllById(productIdList)).thenReturn(null);
//
//        //when
//        //productService.order(userId, productIdWithQuantity);
//
//        //then
//
//        assertThrows(BusinessException.class,
//                () -> productService.order(userId, productIdWithQuantity));
//        verify(productRepository, never()).save(any());
//
//
//    }
//
//
//
//    @DisplayName("포인트 지불(사용) - 실패 - 계정정보 없음")
//    @Test
//    void useTestFailNoMember() {
//        // given
//        long userId = 99L;
//        Long orderId = 1L;
//
//
//        List<Product> productList = new ArrayList<>();
//
//        Product product1 = new Product(1L, "테스트상품1", 1000, 10);
//        Product product2 = new Product(2L, "테스트상품2", 2000, 10);
//        Product product3 = new Product(3L, "테스트상품3", 3000, 10);
//
//        productList.add(product1);
//        productList.add(product2);
//        productList.add(product3);
//
//
//
//        int totalPayPoint = product1.getPruductPrice() + product2.getPruductPrice() + product3.getPruductPrice();
//
//
//        //계정정보 조회      userRepository를 호출하게 되면  빈값을 넘겨라
//        when(userRepository.findByUserId(userId)).thenReturn(null);
//
//
//        // when
//        //productService.paymentPoint(userId, orderId ,totalPayPoint);
//
//        // then
//        assertThrows(BusinessException.class,
//                () -> productService.paymentPoint(userId, orderId ,totalPayPoint));
//        verify(userRepository, never()).save(any()); // 저장 시도 없음 확인
//
//    };
//
//
//
//    @DisplayName("포인트 지불(사용) - 실패 - 잔고없음")
//    @Test
//    void useTestFailNoStock() {
//        // given
//        long userId = 99L;
//        Long orderId = 1L;
//        int userPoints = 0;
//
//        List<Product> productList = new ArrayList<>();
//        List<Long> productIdList = Arrays.asList(1L, 2L, 3L);
//
//
//        Product product1 = new Product(1L, "테스트상품1", 1000, 50);
//        Product product2 = new Product(2L, "테스트상품2", 2000, 50);
//        Product product3 = new Product(3L, "테스트상품3", 3000, 50);
//
//        productList.add(product1);
//        productList.add(product2);
//        productList.add(product3);
//
//        int totalPayPoint = product1.getPruductPrice() + product2.getPruductPrice() + product3.getPruductPrice();
//
//        User user = new User(userId, "testUser", new Point(userPoints));
//
//        //주문상세정보 리스트
//        List<OrderDetail> orderDetailList = new ArrayList<>();
//
//        for (int i = 0; i < productIdList.size(); i++) {
//            OrderDetail orderDetail = new OrderDetail();
//            orderDetail.setOrderDetailId((long) (99-i));
//
//            // Product 객체 생성
//            Product product = new Product();
//            product.setProductId(productIdList.get(i));
//
//            orderDetail.setProduct(product);
//            orderDetail.setProductName("테스트상품"+ (i-1));
//            orderDetail.setQuantity(30+i);
//            orderDetail.setProductPrice(100000 + 100*i);
//            orderDetailList.add(orderDetail);
//        }
//
//
//        Order order = new Order(orderId, user, totalPayPoint, orderDetailList);
//
//        //계정 정보 조회
//        when(userRepository.findByUserId(userId)).thenReturn(user);
//        //주문정보조회
//        when(productRepository.getOrderInfo(userId, orderId)).thenReturn(order);
//        //상품 정보 조회
//        when(productRepository.getProductInfo(1L)).thenReturn(product1);
//        when(productRepository.getProductInfo(2L)).thenReturn(product2);
//        when(productRepository.getProductInfo(3L)).thenReturn(product3);
//
//
//        //상품 정보 리스트 조회
//        when(productRepository.findAllById(productIdList)).thenReturn(productList);
//
//
//        //재고차감 업데이트
//        when(productRepository.decreaseInventory(anyLong(), anyInt()))
//                                                    .thenAnswer(inv -> 1);
//
//
//
//        // when
//        // then
//        BusinessException exception =  assertThrows(BusinessException.class,
//                () -> productService.paymentPoint(userId, orderId ,totalPayPoint));
//
//        assertEquals(ErrorCode.INSUFFICIENT_CASH, exception.getErrorCode());
//        verify(productRepository, never()).save(any()); // 저장 호출되지 않아야 함
//
//
//    };
//
//
//
//    @DisplayName("포인트 지불(사용) - 실패 - 재고 없음")
//    @Test
//    void useTestFailOutStock() {
//
//        // given
//        long userId = 99L;
//        Long orderId = 1L;
//        int userPoints = 0;
//
//        List<Product> productList = new ArrayList<>();
//        List<Long> productIdList = Arrays.asList(1L, 2L, 3L);
//
//
//        Product product1 = new Product(1L, "테스트상품1", 1000, 10);
//        Product product2 = new Product(2L, "테스트상품2", 2000, 10);
//        Product product3 = new Product(3L, "테스트상품3", 3000, 10);
//
//        productList.add(product1);
//        productList.add(product2);
//        productList.add(product3);
//
//        int totalPayPoint = product1.getPruductPrice() + product2.getPruductPrice() + product3.getPruductPrice();
//
//
//        // Point는 User 생성자 내부에 포함됨
//        User user = new User(userId, "testUser", new Point(userPoints));
//
//        //주문상세정보 리스트
//        List<OrderDetail> orderDetailList = new ArrayList<>();
//
//        for (int i = 0; i < productIdList.size(); i++) {
//            OrderDetail orderDetail = new OrderDetail();
//            orderDetail.setOrderDetailId((long) (99-i));
//
//            // Product 객체 생성
//            Product product = new Product();
//            product.setProductId(productIdList.get(i));
//
//            orderDetail.setProduct(product);
//            orderDetail.setProductName("테스트상품"+ (i-1));
//            orderDetail.setQuantity(30+i);
//            orderDetail.setProductPrice(100000 + 100*i);
//            orderDetailList.add(orderDetail);
//        }
//
//
//        Order order = new Order(orderId, user, totalPayPoint, orderDetailList);
//
//        //계정 정보 조회
//        when(userRepository.findByUserId(userId)).thenReturn(user);
//        //주문정보조회
//        when(productRepository.getOrderInfo(userId, orderId)).thenReturn(order);  //요청 수량
//        //상품 정보 조회
//        when(productRepository.getProductInfo(1L)).thenReturn(product1);
//        when(productRepository.getProductInfo(2L)).thenReturn(product2);
//        when(productRepository.getProductInfo(3L)).thenReturn(product3);
//
//
//        //상품 정보 리스트 조회
//        when(productRepository.findAllById(productIdList)).thenReturn(productList); // 재고
//
//
//
//        // when
//
//
//
//        // then
//        BusinessException exception =  assertThrows(BusinessException.class,
//                () -> productService.paymentPoint(userId, orderId ,totalPayPoint));
//
//        assertEquals(ErrorCode.OUT_OF_STOCK, exception.getErrorCode());
//
//
//        verify(productRepository, never()).save(any());
//
//
//
//    }


}