package com.example.newecommerce.user.controller;

import com.example.newecommerce.common.exception.BusinessException;
import com.example.newecommerce.common.exception.ErrorCode;
import com.example.newecommerce.product.domain.ProductRepository;
import com.example.newecommerce.user.application.UserServiceImpl;
import com.example.newecommerce.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {


     /*
   동시에 여러 건의 포인트 충전, 이용 요청이 들어올 경우 순차적으로 처리되어야 합니다.
   잔고가 부족할 경우, 포인트 사용은 실패하여야 합니다.

   patch - 포인트를 충전
    실패
        충전할려는 계정정보가 없어서 실패 없다
        중복충전(더블클릭시) 동작시 동시성 처리가 안되어 있으면 실패

   patch - 포인트를 사용
    실패
        포인트 잔고가 부족 하여실패
        포인트를 사용할 계정정보가 없어서 실패
        중복사용(더블클릭시) 동작시 동시성 처리가 안되어 있으면 실패
        중복사용(더블클릭시) 동작시  OptimisticLockException 낙관적락 예외가 나오지 않으면 실패 - 낙관적사용락 테스트


   get - 포인트를 조회
    실패
        포인트를 가진 계정정보가 없어서 실패


   get - 포인트 내역을 조회
    실패
        포인트를 가진 계정정보가 없어서 실패

   */

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private UserServiceImpl userService;



    @DisplayName("포인트 충전 -  계정정보 없음") //실패 코드 - 예외가 발생하거나 실패 시나리오가 올바르게 처리가 되었는지
    @Test
    void chargeTestFailNoMember() {
        // given
        long userId = 99L;
        int points = 1000;

        //만약에 pointServie.chargePoints안에  userRepository를 호출하게 되면  빈값을 넘겨라
        when(userRepository.findByUserId(userId)).thenReturn(null);

        // when

        // then
        assertThrows(BusinessException.class,
                () -> userService.chargePoint(userId, points));
        verify(userRepository, never()).save(any()); // 저장 시도 없음 확인

    };


    @DisplayName("포인트 조회 - 계정정보 없음") //실패 코드 - 예외가 발생하거나 실패 시나리오가 올바르게 처리가 되었는지
    @Test
    void pointInquiryTestFailNoMember() {
        // given
        long userId = 99L;
        when(userRepository.findByUserId(userId)).thenReturn(null);




        // when
        // then
        BusinessException exception = assertThrows(BusinessException.class,
                                             () -> userService.PointInquiry(userId));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(userRepository).findByUserId(userId);// 조회 시도는 했는지 확인


    };


    @DisplayName("포인트 내역 조회 - 계정정보 없음") //실패 코드 - 예외가 발생하거나 실패 시나리오가 올바르게 처리가 되었는지
    @Test
    void pointHistoryInquiryTestFailNoMember() {
        // given
        long userId = 99L;
        when(userRepository.findByUserId(userId)).thenReturn(null);


        // when
        // then
        BusinessException exception = assertThrows(BusinessException.class,
                                        () -> userService.PointHistoryInquiry(userId));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

        verify(userRepository).findByUserId(userId);// 조회 시도는 했는지 확인


    };







}