package com.example.newecommerce.product.application;


import com.example.newecommerce.common.exception.*;
import com.example.newecommerce.order.domain.Order;
import com.example.newecommerce.order.domain.OrderDetail;
import com.example.newecommerce.product.domain.Product;
import com.example.newecommerce.product.domain.ProductRepository;

import com.example.newecommerce.product.dto.ProductResponse;

import com.example.newecommerce.user.domain.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    @Override
    public List<ProductResponse> inquiryProduct(List<Long> productIdList) {

        List<Product> inProductList = new ArrayList<>();
        List<Product> outProductList = new ArrayList<>();

        List<Product> productList = productRepository.findAllById(productIdList);
        if (productList.isEmpty()) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FIND);
        }


        for (Product product : productList) {

            if(product != null) {
                inProductList.add(product);

            } else {
                throw new BusinessException(ErrorCode.PRODUCT_NOT_FIND);
               // outProductList.add(product);

            }

        }

        //Dto 변환
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : inProductList) {
            ProductResponse response = new ProductResponse(
                    product.getProductId(),
                    product.getProcuctName(),
                    product.getPruductPrice(),
                    product.getInventory()
            );
            productResponseList.add(response);
        }



        return productResponseList;
    }


    @Transactional
    @Override
    public List<ProductResponse> inquiryBestProduct() {


        List<Product> bestProductList = productRepository.bestProductInquiry();
        if(bestProductList == null || bestProductList.isEmpty()) {
           throw new BusinessException(ErrorCode.PRODUCT_NOT_FIND);
        }

        List<ProductResponse> bestProductResponses = new ArrayList<>();
        for (Product product : bestProductList) {
            ProductResponse response = new ProductResponse(
                    product.getProductId(),
                    product.getProcuctName(),
                    product.getPruductPrice(),
                    product.getInventory()
            );
            bestProductResponses.add(response);
        }

        return bestProductResponses;
    }

    @Override
    public boolean productStockDeduct(Order orderInfo, List<Long> productIdList) {



        //주문데이터 - 상품아이디와 구매수량
        HashMap<Long, Integer> productIdWithQuantity = new HashMap<>();

        for(OrderDetail orderDetail : orderInfo.getOrderDetailList()){
            //주문테이블 데이터
            productIdWithQuantity.put(orderDetail.getProduct().getProductId(), orderDetail.getQuantity());

        }


        //상품정보조회
        List<Product> productList = productRepository.findAllById(productIdList);
        List<Product> inStockProductList = new ArrayList<>();
        List<Product> outStockProductList = new ArrayList<>();



        for (Product product : productList) {
            //productList 아이디값으로 밸류값을 가져온다
            int requestQty = productIdWithQuantity.get(product.getProductId());
            if(requestQty == 0) {
                throw new BusinessException(ErrorCode.ORDERINFO_NOT_FIND);
            }

            if (product.getInventory() < requestQty) {
                //재고가 없으면 예외 발생

                outStockProductList.add(product);
                productIdWithQuantity.remove(product.getProductId());

                throw new BusinessException(ErrorCode.OUT_OF_STOCK);

            }else {

                inStockProductList.add(product);

                int updateInventory = product.getInventory() - requestQty;

                //재고 차감로직
                //비관적락 - 배타락 적용
                //int rst = productRepository.decreaseInventory(product.getProductId() ,updateInventory);
                int rst = productRepository.decreaseInventory(product.getProductId() ,updateInventory);
                if(rst == 0){
                    throw new BusinessException(ErrorCode.UNEXPECTED_ERROR);
                }


            }
        }




        return true;
    }


}
