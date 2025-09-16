package com.example.newecommerce.user.domain;

import com.example.newecommerce.common.enums.EnumPointStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@SuperBuilder
@Entity
@Table(name = "point_history")
public class PointHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private Point point;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnumPointStatus status;

    @Column(name = "change_point")
    private int changePoint;

    @Column(name = "remaining_point")
    private int  remainingPoint;

    @Column(name = "date")
    private LocalDateTime date;




}
