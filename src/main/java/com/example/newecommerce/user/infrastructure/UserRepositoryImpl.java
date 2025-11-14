package com.example.newecommerce.user.infrastructure;


import com.example.newecommerce.common.enums.EnumPointStatus;
import com.example.newecommerce.user.domain.Point;
import com.example.newecommerce.user.domain.PointHistory;
import com.example.newecommerce.user.domain.User;
import com.example.newecommerce.user.domain.UserRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User findByUserId(long userId) {
        //유저고유번호를 파라미터로 user테이블과 point테이블을 조인해서 데이터를 가져온다
        List<User> user = em.createQuery(
                    "select u from User u left join fetch u.point p where u.userId = :userId", User.class)
                    .setParameter("userId", userId)
                    .getResultList();

       // SELECT * FROM user u JOIN point p ON u.userId = p.userId

        return user.isEmpty()? null : user.get(0) ;
    }

    @Override
    public int updatePoint(long userId, int additionalPoints) {

        List<Point> point = em.createQuery("select p from Point p join p.user u where u.userId = :userId", Point.class)
                                    .setParameter("userId", userId)
                                    //.setLockMode(LockModeType.PESSIMISTIC_WRITE)
                                    .getResultList();

        //데이터가 없으면 새로 insert
        if (point.isEmpty()) {
            Point pointInfo = new Point();
            User userRef = em.getReference(User.class, userId);

            pointInfo.setUser(userRef);
            pointInfo.setPoint(additionalPoints);

            em.persist(pointInfo);

            return 0;
        }

        point.get(0).setPoint(additionalPoints);

        return 1;

        //UPDATE point p JOIN user u ON p.userId = u.userId SET p.point = theFinalPoint WHERE u.userId = userId;

    }

    //포인트 변경 내역 작성
    @Override
    public int updatePointHistory(long pointId, EnumPointStatus enumPointStatus, int changePoint, int theFinalPoint) {

       PointHistory pointHistory = new PointHistory();
       Point pointRef = em.getReference(Point.class, pointId);

       pointHistory.setPoint(pointRef);
       pointHistory.setStatus(enumPointStatus);
       pointHistory.setChangePoint(changePoint);
       pointHistory.setRemainingPoint(theFinalPoint);
       pointHistory.setDate(LocalDateTime.now());

       em.persist(pointHistory);



       // ??????????????????????????????????????
       return 1;


        /*
            INSERT INTO point_history (
                                    point_id,
                                    status,
                                    change_point,
                                    remaining_point,
                                    date
                                    )
                                SELECT
                                    p.point_id,
                                    #{status},
                                    #{changePoint},
                                    #{theFinalPoint},
                                    NOW()
                                FROM point p
                                WHERE p.point_id = #{pointId};
         */
    }

    @Override
    public List<PointHistory> PointHistoryInquiry(long pointId) {

        List<PointHistory> rst = em.createQuery("select ph from PointHistory ph join fetch ph.point p " +
                           "where p.pointId = :pointId", PointHistory.class)
                           .setParameter("pointId", pointId)
                           .getResultList();




        return rst;



        /*
            SELECT * FROM point_history ph
            JOIN point p ON p.point_id = ph.point_id;
            SET p.point_id = #{pointId}
        */
    }

    @Override
    public Point setPoint(long userId) {

        Point point = new Point();
        User userRef = em.getReference(User.class, userId);

        point.setUser(userRef);
        point.setPoint(0);

        em.persist(point);

        return point;
    }


}
