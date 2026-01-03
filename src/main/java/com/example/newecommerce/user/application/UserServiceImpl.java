package com.example.newecommerce.user.application;

import com.example.newecommerce.common.enums.EnumPointStatus;
import com.example.newecommerce.common.exception.BusinessException;
import com.example.newecommerce.common.exception.ErrorCode;
import com.example.newecommerce.product.domain.Product;
import com.example.newecommerce.product.dto.ProductResponse;
import com.example.newecommerce.user.domain.Point;
import com.example.newecommerce.user.domain.PointHistory;
import com.example.newecommerce.user.domain.User;
import com.example.newecommerce.user.domain.UserRepository;
import com.example.newecommerce.user.dto.PointHistoryResponse;
import com.example.newecommerce.user.dto.PointResponse;
import com.example.newecommerce.user.dto.UserResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PessimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
    public PointResponse chargePoint(long userId, int point) {

        try {
            //계정 정보 조회(아이디, 이름, 현재포인트)
            User user = userRepository.findByUserId(userId);



            //if문 - 계정이 있으면 충전, 업데이트 포인트 테이블, 포인트 이력테이블
            if (user != null && user.getPoint() == null) {

                Point newPoint = userRepository.setPoint(userId);

                Long pointId = newPoint.getPointId();

                int additionalPoints = newPoint.getPoint() + point;
                Point updateResult  = userRepository.optimisticLockUpdatePoint(userId, additionalPoints);
                userRepository.updatePointHistory(pointId, EnumPointStatus.CHARGED, point, additionalPoints);

                //도메인 변환 dto로 변환
                PointResponse pointResponse = new PointResponse(
                            updateResult.getUser().getUserId(),
                            updateResult.getPoint()

                );



                return pointResponse;

            } else if (user != null && user.getPoint() != null) {

                Long pointId = user.getPoint().getPointId();
                int additionalPoints = user.getPoint().getPoint() + point;

                Point updateResult = userRepository.optimisticLockUpdatePoint(userId, additionalPoints);
                userRepository.updatePointHistory(pointId, EnumPointStatus.CHARGED, point, additionalPoints);

                //도메인 변환 dto로 변환
                PointResponse pointResponse = new PointResponse(
                        updateResult.getUser().getUserId(),
                        updateResult.getPoint()

                );

                return pointResponse;

            }else  {
                //else - 없으면 false 반환 혹은 계정없음 커스텀 에러발생
                //throw new BusinessException(ErrorCode.USER_NOT_FOUND);
                //빈값
//                PointResponse pointResponse = new PointResponse();
//                return pointResponse;

                throw new BusinessException(ErrorCode.USER_NOT_FOUND);

            }




        }catch (ObjectOptimisticLockingFailureException e) {

            System.out.println("중복 충전 발생"+ e.getMessage());
            PointResponse pointResponse = new PointResponse();
            return pointResponse;
        }


    }



    @Transactional
    @Override
    public boolean usePoint(long userId, int point) {

        try {
            User user = userRepository.findByUserId(userId);

            if (user != null) {
                Long pointId = user.getPoint().getPointId();
                int additionalPoints = user.getPoint().getPoint() - point;
                userRepository.PessimisticLockUpdatePoint(userId, additionalPoints);
                userRepository.updatePointHistory(pointId, EnumPointStatus.USED, point, additionalPoints);
            }else {
                //throw new BusinessException(ErrorCode.USER_NOT_FOUND)
                return false;
            }

            return true;
        }catch (PessimisticLockException | LockTimeoutException e) {

            System.out.println("락 경합 발생"+ e.getMessage());
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


