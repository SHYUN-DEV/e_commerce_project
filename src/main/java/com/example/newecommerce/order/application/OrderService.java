package com.example.newecommerce.order.application;

import java.util.Map;

public interface OrderService {

    public Long order(Long userId, Map<Long, Integer> productIdWithQuantity);

    public boolean paymentPoint(Long userId, Long orderId, int totalPayPoint);
}
