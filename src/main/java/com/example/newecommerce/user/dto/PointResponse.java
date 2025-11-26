package com.example.newecommerce.user.dto;

import com.example.newecommerce.user.domain.Point;
import com.example.newecommerce.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointResponse {

    private Long userId;
    private int point;

}
