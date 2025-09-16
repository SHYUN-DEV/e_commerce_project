package com.example.newecommerce.cart.application;

import com.example.newecommerce.cart.domain.Cart;
import com.example.newecommerce.cart.dto.CartResponse;

import java.util.List;
import java.util.Map;

public interface CartService {


  public List<CartResponse> cartInquire(Long userId);

  public boolean addCart(Map<Long, Integer> productIdList, Long userId);

  public boolean delcart(Long userId, List<Long> productIdList);



}
