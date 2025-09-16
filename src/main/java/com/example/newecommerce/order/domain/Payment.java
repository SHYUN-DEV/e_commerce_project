package com.example.newecommerce.order.domain;

import com.example.newecommerce.common.enums.EnumPaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


    @Column(name = "total_pay_price")
    private int totoalPayPrice;


    @Column(name = "payment_date")
    private LocalDateTime paymentDate ;


    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private EnumPaymentMethod paymentMethod;



}
