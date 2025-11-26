package com.example.newecommerce.product.controller;


import com.example.newecommerce.product.application.ProductService;
import com.example.newecommerce.product.domain.Product;
import com.example.newecommerce.product.dto.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "상품", description = "상품 관련 기능")
@RestController
@RequestMapping(name = "/product")
public class ProductController {

    public final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }


    @Operation(summary = "상품/상품 조회", description = "상품을 조회합니다")
    @GetMapping("/inquiry")
    public List<ProductResponse> getProducts(@RequestParam List<Long> productIdList) {

        return productService.inquiryProduct(productIdList);

    }

    @Operation(summary = "상품/상위 상품 조회", description = "상위 판매 상품을 조회합니다")
    @GetMapping("/bestProducts")
    public List<ProductResponse> getBestProducts() {

        return productService.inquiryBestProduct();

    }





//    //주문
//    @Operation(summary = "상품/주문", description = "상품을 주문합니다")
//    @PostMapping("point/{id}/order")
//    public Long order(@PathVariable("id") Long userId,
//                  @RequestBody Map<Long, Integer> productIdWithQuantity) {
//        //주문 과정은  계정조회, 재고조회 까지 이고
//        //파라미터는 유저아이디, 상품아이디 구매수량 맵 데이터
//
//
//
//        return productService.order(userId, productIdWithQuantity);
//    };
//
//
//
//    //지불- 포인트 사용
//    @Operation(summary = "상품/결제", description = "주문한 상품을 결제합니다")
//    @PostMapping("point/{id}/payment")
//    public boolean payment(@PathVariable("id") Long userId,
//                          @RequestBody Long orderId,
//                          int totalPayPoint ) {
//
//        //결제 과정
//        //계정조회, 재고조회, 포인트조회, 결제
//
//        //파라미터는 유저아이다, 총 결제할 포인트, 상품아이디 수량 맵 데이터
//
//
//        return productService.paymentPoint(userId, orderId, totalPayPoint);
//    };


}
