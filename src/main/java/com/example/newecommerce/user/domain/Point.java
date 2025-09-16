package com.example.newecommerce.user.domain;

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
//@SuperBuilder
@Entity
@Table(name = "point")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long pointId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "point")
    private int point;

    @Version
    private int version;

    @OneToMany(mappedBy = "point")
    private List<PointHistory> pointHistoryList = new ArrayList<>();


    public Point(int finalPoint) {

        this.point = finalPoint;
    }
}
