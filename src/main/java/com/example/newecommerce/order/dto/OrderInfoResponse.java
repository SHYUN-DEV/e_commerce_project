package com.example.newecommerce.order.dto;

import com.example.newecommerce.common.enums.EnumOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoResponse {

    private Long orderId;
    private Long userId;
    private int totalOrderPrice;
    private String orderStatus;
    private LocalDateTime orderDate;
    private List<OrderDetailResponse> orderDetailList;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetailResponse {
        private Long orderDetailId;
        private Long productId;
        private String productName;
        private int quantity;
        private int productPrice;
        private EnumOrderStatus orderDetailStatus;
        private LocalDateTime orderDetailDate;
    }
}
