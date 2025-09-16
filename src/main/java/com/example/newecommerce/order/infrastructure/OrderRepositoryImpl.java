package com.example.newecommerce.order.infrastructure;

import com.example.newecommerce.common.enums.EnumOrderStatus;
import com.example.newecommerce.common.enums.EnumPaymentMethod;
import com.example.newecommerce.order.domain.Order;
import com.example.newecommerce.order.domain.OrderDetail;
import com.example.newecommerce.order.domain.OrderRepositoryCustom;
import com.example.newecommerce.order.domain.Payment;
import com.example.newecommerce.product.domain.Product;
import com.example.newecommerce.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public void updateOrderDetailProductName(Long orderDetailProductId, long orderId, String procuctName) {
        em.createQuery("update  OrderDetail od set od.productName = :productName " +
                        "where od.product.productId = :productId and od.order.orderId = :orderId")
                .setParameter("productName", procuctName)
                .setParameter("productId", orderDetailProductId)
                .setParameter("orderId", orderId)
                .executeUpdate();



        /*
        UPDATE order_detail
        SET product_name = #{procuctName}
        WHERE product_id = #{orderDetailProductId} AND order_id = #{orderId};
         */
    }

    @Override
    public void updateOrderDetailProductprice(Long orderDetailProductId, long orderId, int pruductPrice) {
        em.createQuery("update OrderDetail od set od.productPrice = :pruductPrice " +
                        "where od.product.productId = :productId and od.order.orderId = :orderId")
                .setParameter("pruductPrice", pruductPrice)
                .setParameter("productId", orderDetailProductId)
                .setParameter("orderId", orderId)
                .executeUpdate();

         /*
            UPDATE order_detail
            SET product_price = #{pruductPrice}
            WHERE product_id = #{orderDetailProductId} AND order_id = #{orderId};
         */


    }

    @Override
    public void updateOrderStatus(long userId, long orderId, EnumOrderStatus orderStatus) {
        em.createQuery("update Order o set o.orderDate = current_timestamp , o.orderStatus = :status " +
                        "where o.user.userId = :userId and o.orderId = :orderId")
                .setParameter("status", orderStatus)
                .setParameter("userId", userId)
                .setParameter("orderId", orderId)
                .executeUpdate();

        em.createQuery("update OrderDetail od set od.orderDetailDate = current_timestamp, od.orderDetailStatus = :status " +
                        "where od.order.user.userId = :userId and od.order.orderId = :orderId")
                .setParameter("status", orderStatus)
                .setParameter("userId", userId)
                .setParameter("orderId", orderId)
                .executeUpdate();




        /*
           UPDATE `order` o
           JOIN order_detail od ON od.order_id = o.order_id
           SET o.order_date = NOW(),
           o.order_status = EnumOrderStatus.success,
           od.order_detail_date = NOW(),
           od.order_detail_status = EnumOrderStatus.success
           WHERE
           o.user_id = #{userId}
           AND o.order_id = #{orderId};
         */

    }

    @Override
    public boolean saveOrder(long userId, int totalPayPrice, EnumOrderStatus enumOrderStatus) {
        //유저 조회
        User user = em.find(User.class, userId);
        if (user == null) {
            return false; // 혹은 예외 던지기
        }

        //주문 엔티티 생성
        Order order = new Order();
        order.setUser(user);
        order.setTotalOrderPrice(totalPayPrice);
        order.setOrderStatus(enumOrderStatus);
        order.setOrderDate(LocalDateTime.now());

        //저장 (JPQL의 INSERT 대체)
        em.persist(order);

        return true;


//        INSERT INTO `order` (
//                    user_id,
//                    total_order_price,
//                    order_status,
//                    order_date
//                    ) VALUES (
//                                #{userId},
//                                #{totalPayPrice},
//                                'PENDING',
//                                NOW()
//                    );
    }

    @Override
    public Long getOrderId(long userId, EnumOrderStatus enumOrderStatus) {

        List<Long> orderIds = em.createQuery("SELECT o.orderId FROM Order o WHERE o.user.userId = :userId AND o.orderStatus = :status")
                .setParameter("userId", userId)
                .setParameter("status", enumOrderStatus)
                .getResultList();

        Long orderId = orderIds.isEmpty() ? null : orderIds.get(0);


        /*
            select order_id from order
            where user_id = #{userId}
            and order_status = #{enumOrderStatus}
         */


        return orderId;
    }

    @Override
    public boolean saveOrderDetail(Long orderId, Product product, int requestQty, EnumOrderStatus enumOrderStatus) {


        // 주문 엔티티 조회 - 1차캐시에 없으면 db에서 조회
        Order order = em.find(Order.class, orderId);
        if (order == null) {
            return false;
        }

        // 주문 상세 엔티티 생성
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setProductName(product.getProcuctName()); // 주문 시점의 상품명
        orderDetail.setQuantity(requestQty); // 필요 시 파라미터로 수량 전달
        orderDetail.setProductPrice(product.getPruductPrice()); // 주문 시점의 가격
        orderDetail.setOrderDetailStatus(enumOrderStatus);
        orderDetail.setOrderDetailDate(LocalDateTime.now());

        // 저장
        em.persist(orderDetail);

        return true;

        /*

                INSERT INTO order_detail (order_detail_id,
                                          order_id,
                                          product_id,
                                          product_name,
                                          quantity,
                                          product_price,
                                          order_detail_status,
                                          order_detail_date
                                          ) VALUES
                                         <foreach collection="orderDetailList" item="item" separator=",">
                                                                        ( #{item.orderDetailId},
                                                                          #{orderId},
                                                                          #{item.productId},
                                                                          #{item.productName},
                                                                          #{item.quantity},
                                                                          #{item.productPrice},
                                                                          #{orderDetailStatus},
                                                                          NOW()
                                                                          )
                                          </foreach>

         */


    }

    @Override
    public Order getOrderInfo(long userId, long orderId) {
        List<Order> orders = em.createQuery(
                        "select distinct o from Order o " +
                                "join fetch o.orderDetailList od " +
                                "where o.user.userId = :userId and o.orderId = :orderId and o.orderStatus = :status " +
                                "order by od.product.productId asc",Order.class)
                .setParameter("userId", userId)
                .setParameter("orderId", orderId)
                .setParameter("status", EnumOrderStatus.PENDING)
                .getResultList();

        return orders.isEmpty() ? null : orders.get(0);

//        SELECT o.*, od.*
//                FROM `order` o
//        JOIN order_detail od ON od.order_id = o.order_id
//        WHERE o.user_id = #{userId} AND o.order_id = #{orderId}
//        AND o.order_status = "PENDING"
//        ORDER BY od.product_id ASC;
    }

    //결제테이블 업데이트
    @Override
    public void updatePayment(Long orderId, int totalPayPoint, EnumPaymentMethod enumPaymentMethod) {
        Payment payment = new Payment();
        Order orderRef = em.getReference(Order.class, orderId);

        payment.setOrder(orderRef);
        payment.setTotoalPayPrice(totalPayPoint);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(enumPaymentMethod);

        em.persist(payment);





        /*
           insert into payment(order_id,
                               total_pay_price,
                               payment_date,
                               payment_method
                               )values(
                                       #{orderId},
                                       #{totalPayPoint},
                                       NOW(),
                                       #{enumOrderStatus}
                                       )



         */
    }
}
