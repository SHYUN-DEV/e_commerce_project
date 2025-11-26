package com.example.newecommerce.order.controller;

import com.example.newecommerce.order.application.OrderService;
import com.example.newecommerce.order.dto.PaymentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "구매", description = "구매 관련 기능")
@RestController
@RequestMapping(name = "/order")
public class OrderController {

    public final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }


    //주문
    @Operation(summary = "구매/주문", description = "상품을 주문합니다")
    @PostMapping("/{id}")
    public Long order(@PathVariable("id") Long userId,
                      @RequestBody Map<Long, Integer> productIdWithQuantity) {



        //주문 과정은  계정조회, 재고조회 까지 이고
        return orderService.order(userId, productIdWithQuantity);
    };



    //지불- 포인트 사용
    @Operation(summary = "구매/결제", description = "주문한 상품을 결제합니다")
    @PostMapping("/{id}/payment")
    public boolean payment(@PathVariable("id") Long userId,
                           @RequestBody PaymentRequest paymentRequest) {






        //결제과정 계정조회, 재고조회, 포인트조회, 결제
        return orderService.paymentPoint(userId, paymentRequest.getOrderId(), paymentRequest.getTotalPayPoint());
    };



}
