package com.example.newecommerce.cart.application;

import com.example.newecommerce.cart.domain.Cart;
import com.example.newecommerce.cart.domain.CartRepository;
import com.example.newecommerce.cart.dto.CartResponse;
import com.example.newecommerce.common.exception.BusinessException;
import com.example.newecommerce.common.exception.ErrorCode;
import com.example.newecommerce.product.domain.Product;
import com.example.newecommerce.product.domain.ProductRepository;
import com.example.newecommerce.user.domain.User;
import com.example.newecommerce.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {



    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    @Transactional
    @Override
    public List<CartResponse> cartInquire(Long userId) {

        //계정 조회
        User user = userRepository.findByUserId(userId);

        if(user == null){

            throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        }

        List<Cart> cartList = cartRepository.cartInquire(userId);
        if (cartList == null || cartList.isEmpty()) {

            //Collections.emptyList() or new ArrayList<>()
            throw new BusinessException(ErrorCode.CARTINFO_NOT_FIND);

        }


        List<CartResponse> cartDtoList = new ArrayList<>();

        //Dto 변환
        for (Cart cart : cartList) {



            CartResponse.Product productDto = new CartResponse.Product(
                    cart.getProduct().getProductId(),
                    cart.getProduct().getProcuctName(),
                    cart.getProduct().getPruductPrice(),
                    cart.getProduct().getInventory()

            );

            CartResponse cartresponse = new CartResponse(
                    cart.getCartId(),
                    productDto,
                    cart.getUser().getUserId(),
                    cart.getQuantity(),
                    cart.getPurchaseStatus()



            );

            cartDtoList.add(cartresponse);


        }


        return cartDtoList;
    }


    @Transactional
    @Override
    public boolean addCart(Map<Long, Integer> productIdQuantity, Long userId) {

        List<Product> inStockList = new ArrayList<>();
        List<Product> outOfStockList = new ArrayList<>();

        //계정조회 널체크
       User user = userRepository.findByUserId(userId);

       if(user == null){
           throw new BusinessException(ErrorCode.USER_NOT_FOUND);
       }


       List<Long> productIdList = new ArrayList<>(productIdQuantity.keySet());



       //상품 정보조회 재고체크
       List<Product> productList = productRepository.findAllById(productIdList);

       if(productList == null){
           throw new BusinessException(ErrorCode.PRODUCT_NOT_FIND);
       }

       for(Product product : productList)
           if(product.getInventory() > 0) {
               inStockList.add(product);

           }else {
               outOfStockList.add(product);
               throw new BusinessException(ErrorCode.OUT_OF_STOCK);
           }



       //장바구니 업데이트
       productIdQuantity.forEach((productId, quantity) -> {


           Cart rst = cartRepository.addCart(productId, quantity, userId);

            if(rst == null){
            throw new BusinessException(ErrorCode.CART_ADD_FAIL);
            }

        });



       return true;
    }


    @Transactional
    @Override
    public boolean delcart(Long userId, List<Long> productIdList) {

        //계정 정보 조회
        User user = userRepository.findByUserId(userId);

        if(user == null){
            return false;
        }



        for (Long productId : productIdList) {

           boolean rst = cartRepository.deleteCart(userId, productId);
           if(!rst){
               throw new BusinessException(ErrorCode.CART_DELETE_FAIL);
           }

        }



        return true;
    }


}



