package com.example.newecommerce.product.domain;

import com.example.newecommerce.common.enums.EnumOrderStatus;
import com.example.newecommerce.order.dto.OrderInfoResponse;

import java.util.List;

public interface ProductRepositoryCustom {

    public List<Product> bestProductInquiry();

    public Product getProductInfo(Long orderDetailProductId);

    public int decreaseInventory(Long productId, int updateInventory);
}
