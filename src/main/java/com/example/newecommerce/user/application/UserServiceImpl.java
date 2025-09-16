package com.example.newecommerce.user.application;

import com.example.newecommerce.common.enums.EnumPointStatus;
import com.example.newecommerce.common.exception.BusinessException;
import com.example.newecommerce.common.exception.ErrorCode;
import com.example.newecommerce.user.domain.PointHistory;
import com.example.newecommerce.user.domain.User;
import com.example.newecommerce.user.domain.UserRepository;
import com.example.newecommerce.user.dto.PointHistoryResponse;
import com.example.newecommerce.user.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Transactional
    @Override
    public boolean chargePoint(long userId, int point) {

        //계정 정보 조회(아이디, 이름, 현재포인트)
        User user = userRepository.findByUserId(userId);



        try {
            //if문 - 계정이 있으면 충전, 업데이트 포인트 테이블, 포인트 이력테이블
            if (user != null) {
                Long pointId = user.getPoint().getPointId();

                int additionalPoints = user.getPoint().getPoint() + point;
                userRepository.updatePoint(userId, additionalPoints);
                userRepository.updatePointHistory(pointId, EnumPointStatus.CHARGED, point, additionalPoints);
                return true;
            }else  {
                //else - 없으면 false 반환 혹은 계정없음 커스텀 에러발생
                throw new BusinessException(ErrorCode.USER_NOT_FOUND);
            }

                    //동시성제어 낙관적락 에러
        }catch (ObjectOptimisticLockingFailureException e) {

            System.out.println("중복 충전 발생"+ e.getMessage());

            return false;
        }



    }





    @Override
    public UserResponse PointInquiry(long userId) {

        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }



        //dto변환
        UserResponse.Point pointDto = new UserResponse.Point(
                user.getPoint().getPointId(),
                user.getPoint().getPoint()

        );

        UserResponse userResponse = new UserResponse(
                user.getUserId(),
                user.getUserName(),
                pointDto

        );




        return userResponse;
    }

    @Override
    public List<PointHistoryResponse> PointHistoryInquiry(long userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        Long pointId = user.getPoint().getPointId();

        List<PointHistoryResponse> pointHistoryResponses = new ArrayList<>();

        List<PointHistory> pointHistoryList = userRepository.PointHistoryInquiry(pointId);

            //Dto 변환
            for(PointHistory pointHistory : pointHistoryList){

                    PointHistoryResponse.Point pointDto = new PointHistoryResponse.Point(
                                                                              pointHistory.getPoint().getPointId(),
                                                                              pointHistory.getPoint().getUser().getUserId(),
                                                                              pointHistory.getPoint().getPoint()



                    );


                    PointHistoryResponse pointHistoryResponse = new PointHistoryResponse(
                                                                              pointHistory.getHistoryId(),
                                                                              pointHistory.getStatus(),
                                                                              pointHistory.getChangePoint(),
                                                                              pointHistory.getRemainingPoint(),
                                                                              pointHistory.getDate(),
                                                                              pointDto
                    );


            pointHistoryResponses.add(pointHistoryResponse);

            }




        return pointHistoryResponses;
    }


}


