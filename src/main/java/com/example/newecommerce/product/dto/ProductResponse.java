package com.example.newecommerce.product.dto;


import com.example.newecommerce.common.enums.EnumOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long productId;
    private String productName;
    private int productPrice;
    private int inventory;


}
