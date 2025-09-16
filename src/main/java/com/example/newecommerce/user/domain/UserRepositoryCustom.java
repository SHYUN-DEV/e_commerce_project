package com.example.newecommerce.user.domain;
import com.example.newecommerce.common.enums.EnumPointStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepositoryCustom {

    public User findByUserId(long userId);

    public int updatePoint(long userId, int theFinalPoint);

    public int updatePointHistory(long pointId, EnumPointStatus enumPointStatus, int changePoint, int theFinalPoint);

    public List<PointHistory> PointHistoryInquiry(long pointId);

}
