package com.example.newecommerce.product.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String procuctName;

    @Column(name = "product_price")
    private int pruductPrice;

    @Column(name = "inventory")
    private int inventory;

//    @OneToMany(mappedBy = "product")
//    private List<Cart> cartList = new ArrayList<>();

//     @OneToMany(mappedBy = "prodcut")
//     private List<OrderDetail> orderDetailList = new ArrayList<>();






//    public void decreaseInventory(int quantity) {
//        if (quantity <= 0) {
//            throw new IllegalArgumentException("차감 수량은 1 이상이어야 합니다.");
//        }
//
//        if (this.inventory < quantity) {
//            throw new IllegalStateException("재고가 부족합니다. (현재 재고: " + this.inventory + ", 요청 수량: " + quantity + ")");
//        }
//
//        this.inventory -= quantity;
//    }


}
