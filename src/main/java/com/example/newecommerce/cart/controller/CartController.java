package com.example.newecommerce.cart.controller;

import com.example.newecommerce.cart.application.CartService;
import com.example.newecommerce.cart.domain.Cart;
import com.example.newecommerce.cart.dto.CartResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "장바구니", description = "장바구니 관련 기능")
@RestController
@RequestMapping(name = "/cart")
public class CartController {

    public final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @Operation(summary = "장바구니/상품 조회", description = "장바구니 상품을 조회합니다")
    @GetMapping("/{id}")
    public List<CartResponse> CartInquiry(@PathVariable("id") Long userId) {

        return cartService.cartInquire(userId);
    }

    @Operation(summary = "장바구니/상품 추가", description = "장바구니 상품을 추가합니다")
    @PostMapping("/{id}/add")
    public boolean CartAdd(@PathVariable("id") Long userId,
                          @RequestBody Map<Long, Integer> productIdQuantity) {


        return cartService.addCart(productIdQuantity, userId);
    }

    @Operation(summary = "장바구니/상품 삭제", description = "장바구니 상품을 삭제합니다")
    @DeleteMapping("/{id}/delete")
    public boolean CartDel(@PathVariable("id") Long userId,
                        @RequestBody List<Long> productIds) {

        return cartService.delcart(userId, productIds);
    }

}
