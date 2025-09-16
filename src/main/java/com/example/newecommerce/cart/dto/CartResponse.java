package com.example.newecommerce.cart.dto;

import com.example.newecommerce.common.enums.EnumPurchaseStatus;
import com.example.newecommerce.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

    private Long cartId;
    private Product product;
    private Long userId;
    private int Quantity;
    private EnumPurchaseStatus purchaseStatus;;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product{
        private Long productId;
        private String productName;
        private int productPrice;
        private int inventory;
    }






}
