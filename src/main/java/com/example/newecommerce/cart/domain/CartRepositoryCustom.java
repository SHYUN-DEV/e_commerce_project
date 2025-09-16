package com.example.newecommerce.cart.domain;


import java.util.List;

public interface CartRepositoryCustom {

   public List<Cart> cartInquire(Long userId);

   public Cart addCart(Long productId, int quantity, Long userId);

   public boolean deleteCart(Long userId, Long productId);
}
