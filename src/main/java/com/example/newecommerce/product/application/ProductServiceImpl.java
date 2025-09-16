package com.example.newecommerce.product.application;


import com.example.newecommerce.common.exception.*;
import com.example.newecommerce.product.domain.Product;
import com.example.newecommerce.product.domain.ProductRepository;

import com.example.newecommerce.product.dto.ProductResponse;

import com.example.newecommerce.user.domain.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

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




}
