package com.example.newecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private long userId;
    private String userName;
    private Point point;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Point {
        private Long pointId;
        private int point;

    }


}
