package com.example.newecommerce.cart.domain;

import com.example.newecommerce.common.enums.EnumPurchaseStatus;
import com.example.newecommerce.product.domain.Product;
import com.example.newecommerce.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "quantity")
    private int Quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_status")
    private EnumPurchaseStatus purchaseStatus;




}
