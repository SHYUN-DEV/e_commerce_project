package com.example.newecommerce.order.application;

import com.example.newecommerce.common.enums.EnumOrderStatus;
import com.example.newecommerce.common.enums.EnumPaymentMethod;
import com.example.newecommerce.common.enums.EnumPointStatus;
import com.example.newecommerce.common.enums.EnumPurchaseStatus;
import com.example.newecommerce.common.exception.BusinessException;
import com.example.newecommerce.common.exception.ErrorCode;


import com.example.newecommerce.order.domain.Order;
import com.example.newecommerce.order.domain.OrderDetail;
import com.example.newecommerce.order.domain.OrderRepository;
import com.example.newecommerce.product.application.ProductService;
import com.example.newecommerce.product.domain.Product;
import com.example.newecommerce.product.domain.ProductRepository;
import com.example.newecommerce.user.application.UserService;
import com.example.newecommerce.user.domain.User;
import com.example.newecommerce.user.domain.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService{



    public final OrderRepository orderRepository;
    public final ProductRepository productRepository;
    public final UserRepository userRepository;

    public final UserService userService;
    public final ProductService productService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, UserService userService, ProductService productService){
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;

        this.userService = userService;
        this.productService = productService;
    }



    @Transactional
    @Override
    public Long order(Long userId, Map<Long, Integer> productIdWithQuantity) {


        //계정조회 계정정보 / 없으면 에러
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        List<Long> productIds = new ArrayList<>(productIdWithQuantity.keySet());

        //상품정보조회
        List<Product> productList = productRepository.findAllById(productIds);
        if (productList == null || productList.isEmpty()) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FIND);
        }

        List<Product> inStockProductList = new ArrayList<>();
        List<Product> outStockProductList = new ArrayList<>();
        int totalPayPrice = 0;

        //재고 체크
        for (Product product : productList) {
            //productList 아이디값으로 밸루값을 가져온다
            int requestQty = productIdWithQuantity.get(product.getProductId());
            totalPayPrice += (product.getPruductPrice() * requestQty);

            if (product.getInventory() < requestQty) {
                outStockProductList.add(product);
                productIdWithQuantity.remove(product.getProductId());
                //재고가 없으면 제외하고 알림을 줘야 ..........!!!!!1
            }else if (product.getInventory() >= requestQty) {
                inStockProductList.add(product);

            }else {
                throw new BusinessException(ErrorCode.OUT_OF_STOCK);

            }
        }

        //주문테이블 저장
        boolean saveOrderRst = orderRepository.saveOrder(userId, totalPayPrice, EnumOrderStatus.PENDING);
        if (!saveOrderRst) {
            throw new BusinessException(ErrorCode.UNEXPECTED_ERROR);
        }

        //주문아이디 가져오기
        Long orderId =  orderRepository.getOrderId(userId, EnumOrderStatus.PENDING);


        for (Product product : inStockProductList) {
            //구매수량
            int requestQty = productIdWithQuantity.get(product.getProductId());
            //주문상세 저장             구매수량을......
            boolean saveOrderDetailRst = orderRepository.saveOrderDetail(orderId, product, requestQty, EnumOrderStatus.PENDING);

            if (!saveOrderDetailRst) {
                throw new BusinessException(ErrorCode.UNEXPECTED_ERROR);

            }

        }


        return orderId;




    }

    @Transactional
    @Override
    public boolean paymentPoint(Long userId, Long orderId, int totalPayPoint) {


        //상품 아이디 리스트
        List<Long>  productIdList = new ArrayList<>();

        //계정정보 조회
        User user = userRepository.findByUserId(userId);

        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }



        //주문정보 조회
        Order orderInfo = orderRepository.getOrderInfo(userId, orderId);


        if(orderInfo.getOrderId() == null){
            throw new BusinessException(ErrorCode.ORDERINFO_NOT_FIND);

        }



        //주문당시데이터와 현재 상품데이터 비교
        for(OrderDetail orderDetail : orderInfo.getOrderDetailList()){

            Long orderDetailProductId = orderDetail.getProduct().getProductId();

            //상품아이디 리스트
            productIdList.add(orderDetailProductId);

            //상품데이터                                             //주문정보데이터 상품아이디로
            Product oneProduct = productRepository.getProductInfo(orderDetailProductId);


            //비교  //상품테이블 데이터                     //주문정보데이터의 상품이름
            if(!oneProduct.getProcuctName().equals(orderDetail.getProductName())){
                // 상품이름이 다르면 orderDetail에 업데이트를 한다
                orderRepository.updateOrderDetailProductName(orderDetailProductId, orderId, oneProduct.getProcuctName());


            } else if (oneProduct.getPruductPrice() != orderDetail.getProductPrice()) {
                orderRepository.updateOrderDetailProductprice(orderDetailProductId, orderId, oneProduct.getPruductPrice());


            }


        }


        //재고차감 호출
        boolean rst = productService.productStockDeduct(orderInfo, productIdList);




        //포인트 차감
        int remainPoint = user.getPoint().getPoint() - totalPayPoint;

        if(remainPoint >= 0) {
            /// ///////////////업데이트 수정 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //동시성제어 - 포인트차감 - 비관적 - 배타락

            userService.usePoint(userId, totalPayPoint);
//            userRepository.updatePoint(userId, remainPoint);
//            userRepository.updatePointHistory(user.getPoint().getPointId(), EnumPointStatus.USED, totalPayPoint, remainPoint);
            //주문상세,주문 테이블 상태시간 업데이트
            //pending일때만 없데이트하게 수정 해야 합니다
            orderRepository.updateOrderStatus(userId, orderId, EnumOrderStatus.COMPLETED);
            //결제테이블 없데이트
            orderRepository.updatePayment(orderId, totalPayPoint, EnumPaymentMethod.POINT);



        }else {
            throw new BusinessException(ErrorCode.INSUFFICIENT_CASH);
        }



        return true;
    }


}
