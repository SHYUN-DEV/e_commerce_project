package com.example.newecommerce.user.application;


import com.example.newecommerce.product.domain.Product;
import com.example.newecommerce.user.domain.Point;
import com.example.newecommerce.user.domain.User;
import com.example.newecommerce.user.domain.PointHistory;
import com.example.newecommerce.user.domain.UserRepository;
import com.example.newecommerce.user.dto.PointHistoryResponse;
import com.example.newecommerce.user.dto.PointResponse;
import com.example.newecommerce.user.dto.UserResponse;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface UserService {


    public PointResponse chargePoint(long userId, int point);


    public boolean usePoint(long userId, int point);


    public UserResponse PointInquiry(long userId);


    public List<PointHistoryResponse> PointHistoryInquiry(long userId);
}
