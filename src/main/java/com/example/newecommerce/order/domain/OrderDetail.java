package com.example.newecommerce.order.domain;

import com.example.newecommerce.common.enums.EnumOrderStatus;
import com.example.newecommerce.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "order_dateil")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "product_price")
    private int productPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_detail_status")
    private EnumOrderStatus orderDetailStatus;

    @Column(name = "order_detail_date")
    private LocalDateTime orderDetailDate;

}
