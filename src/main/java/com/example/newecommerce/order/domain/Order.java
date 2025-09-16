package com.example.newecommerce.order.domain;

import com.example.newecommerce.common.enums.EnumOrderStatus;
import com.example.newecommerce.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_order_price")
    private int totalOrderPrice;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private EnumOrderStatus orderStatus;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetailList = new ArrayList<>();


    public Order(Long orderId, User userId, int totalPayPoint, List<OrderDetail> orderDetailList) {
        this.orderId = orderId;
        this.user = userId;
        this.totalOrderPrice = totalPayPoint;
        this.orderDetailList = orderDetailList;
    }
}
