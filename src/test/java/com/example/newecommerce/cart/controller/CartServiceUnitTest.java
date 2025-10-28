package com.example.newecommerce.cart.controller;

import com.example.newecommerce.cart.application.CartServiceImpl;
import com.example.newecommerce.cart.domain.CartRepository;
import com.example.newecommerce.common.exception.BusinessException;
import com.example.newecommerce.common.exception.ErrorCode;
import com.example.newecommerce.product.domain.Product;
import com.example.newecommerce.product.domain.ProductRepository;
import com.example.newecommerce.user.domain.Point;
import com.example.newecommerce.user.domain.User;
import com.example.newecommerce.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceUnitTest {

    /*
    실패 테스트 코드란 --- 예외상황 이나 실패 상황(시나리오)이 올바르게 처리가 되었는지 확인하는 테스트코드

    장바구니 조회
    실패  - 계정정보 없음

    장바구니 상품 추가
    실패 - 계정정보 없음
    실패 - 상품 정보 없음
    실패 - 상품 재고 부족

    장바구니 상품 삭제
    실패 - 계정정보 없음
    실패 - 상품정보 없음
    */

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;



    @DisplayName("장바구니 조회 - 실패 - 계정정보 없음")
    @Test
    void cartInquiryFailNoMember() {

       //given
       Long userId = 99L;


       when(userRepository.findByUserId(userId)).thenReturn(null);


       //when
       //then
       BusinessException businessException = assertThrows(BusinessException.class,
                                                 () -> cartService.cartInquire(userId));

       assertEquals(ErrorCode.USER_NOT_FOUND, businessException.getErrorCode());
       verify(cartRepository, never()).cartInquire(anyLong());



    }

    @DisplayName("장바구니 추가 - 실패 - 계정정보 없음")
    @Test
    void cartAddFailNoMember() {

        //given
        Long userId = 99L;

        HashMap<Long, Integer> productIdQuantity = new HashMap<>();
        productIdQuantity.put(1L, 5);
        productIdQuantity.put(2L, 24);
        productIdQuantity.put(3L, 3);


        when(userRepository.findByUserId(userId)).thenReturn(null);


        //when
        //then
        BusinessException businessException = assertThrows(BusinessException.class,
                                                    () -> cartService.addCart(productIdQuantity, userId));

        assertEquals(ErrorCode.USER_NOT_FOUND, businessException.getErrorCode());
        verify(cartRepository, never()).addCart(anyLong(), anyInt(), anyLong());




    }

    @DisplayName("장바구니 추가 - 실패 - 상품정보 없음")
    @Test
    void cartAddFailNoProduct() {

        //given
        Long userId = 99L;
        int userPoints = 0;

        User user = new User(userId, "testUser", new Point(userPoints));


        List<Long> productIdList = new ArrayList<>();
        productIdList.add(1L);
        productIdList.add(2L);
        productIdList.add(3L);

        HashMap<Long, Integer> productIdQuantity = new HashMap<>();
        productIdQuantity.put(1L, 5);
        productIdQuantity.put(2L, 24);
        productIdQuantity.put(3L, 3);


        when(userRepository.findByUserId(userId)).thenReturn(user);

        when(productRepository.findAllById(productIdList)).thenReturn(null);


        //when
        //then
        BusinessException exception = assertThrows(BusinessException.class,
                                            () -> cartService.addCart(productIdQuantity, userId));


        assertEquals(ErrorCode.PRODUCT_NOT_FIND, exception.getErrorCode());
        verify(cartRepository, never()).addCart(anyLong(), anyInt(), anyLong());





    }

    @DisplayName("장바구니 추가 - 실패 - 상품재고없음")
    @Test
    void cartAddFailNoQuantity() {

        //given
        Long userId = 99L;
        int userPoints = 0;

        User user = new User(userId, "testUser", new Point(userPoints));


        List<Long> productIdList = new ArrayList<>();
        productIdList.add(1L);
        productIdList.add(2L);
        productIdList.add(3L);

        HashMap<Long, Integer> productIdQuantity = new HashMap<>();
        productIdQuantity.put(1L, 5);
        productIdQuantity.put(2L, 24);
        productIdQuantity.put(3L, 3);

        List<Product> productList = new ArrayList<>();

        productList.add(new Product(1L, "테스트상품1", 1000, 0));
        productList.add(new Product(2L, "테스트상품2", 3000, 50));
        productList.add(new Product(3L, "테스트상품3", 5000, 20));



        when(userRepository.findByUserId(userId)).thenReturn(user);

        when(productRepository.findAllById(productIdList)).thenReturn(productList);


        //when
        //then
        BusinessException exception = assertThrows(BusinessException.class,
                                         () -> cartService.addCart(productIdQuantity, userId));


        assertEquals(ErrorCode.OUT_OF_STOCK, exception.getErrorCode());
        verify(cartRepository, never()).addCart(anyLong(), anyInt(), anyLong());



    }





    @DisplayName("장바구니 삭제 - 실패 - 계정정보 없음")
    @Test
    void cartDelFailNoMember() {
        //given
        Long userId = 99L;

        List<Long> productIdList = new ArrayList<>();
        productIdList.add(1L);
        productIdList.add(2L);
        productIdList.add(3L);


        when(userRepository.findByUserId(userId)).thenReturn(null);

        //when
        boolean rst = cartService.delcart(userId, productIdList);


        //then
        assertFalse(rst);
        verify(cartRepository, never()).deleteCart(anyLong(), anyLong());


    }



    @DisplayName("장바구니 삭제 - 실패 - 상품정보 없음 ")
    @Test
    void cartDelFailNoProduct() {


        //given
        Long userId = 99L;
        int userPoints = 1000000;

        User user = new User(userId, "테스트유저1", new Point(userPoints));

        List<Long> productIdList = new ArrayList<>();

        productIdList.add(1L);
        productIdList.add(2L);
        productIdList.add(3L);


        when(userRepository.findByUserId(userId)).thenReturn(user);


        //when
        //then
        BusinessException exception = assertThrows(BusinessException.class,
                                      () -> cartService.delcart(userId, productIdList));


        assertEquals(ErrorCode.CART_DELETE_FAIL, exception.getErrorCode());
        verify(cartRepository, times(1)).deleteCart(anyLong(), anyLong());





    }
}