package com.example.newecommerce.user.dto;

import com.example.newecommerce.common.enums.EnumPointStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointHistoryResponse {

    private Long historyId;
    private EnumPointStatus status;
    private int change_point;
    private int remaining_point;
    private LocalDateTime date;
    private Point point;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Point {
        private Long pointId;
        private Long userId;
        private int point;


    }


}
