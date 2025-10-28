package com.example.newecommerce.order.domain;

import com.example.newecommerce.common.enums.EnumOrderStatus;
import com.example.newecommerce.common.enums.EnumPaymentMethod;
import com.example.newecommerce.product.domain.Product;

public interface OrderRepositoryCustom {

    public void updateOrderDetailProductName(Long orderDetailProductId, long orderId, String procuctName);

    public void updateOrderDetailProductprice(Long orderDetailProductId, long orderId, int pruductPrice);

    public void updateOrderStatus(long userId, long orderId, EnumOrderStatus orderStatus);

    public boolean saveOrder(long userId, int totalPayPrice, EnumOrderStatus enumOrderStatus);

    public Long getOrderId(long userId, EnumOrderStatus enumOrderStatus);

    public boolean saveOrderDetail(Long orderId, Product product, int requestQty, EnumOrderStatus enumOrderStatus);

    public Order getOrderInfo(long userId, long orderId);

    public void updatePayment(Long orderId, int totalPayPoint, EnumPaymentMethod enumPaymentMethod);


}
