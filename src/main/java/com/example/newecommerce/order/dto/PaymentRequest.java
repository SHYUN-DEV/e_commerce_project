package com.example.newecommerce.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequest {

    private Long orderId;
    private int totalPayPoint;

}
