package com.example.newecommerce.cart.infrastructure;

import com.example.newecommerce.cart.domain.Cart;
import com.example.newecommerce.cart.domain.CartRepositoryCustom;
import com.example.newecommerce.common.enums.EnumPurchaseStatus;
import com.example.newecommerce.product.domain.Product;
import com.example.newecommerce.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartRepositoryImpl  implements CartRepositoryCustom {


    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Cart> cartInquire(Long userId) {
        List<Cart> rst = em.createQuery("select c from Cart c " +
                                        "join fetch c.product p " +
                                        "where c.user.userId = :userId " +
                                        "and c.purchaseStatus = 'NOT_PURCHASED' ", Cart.class)
                                        .setParameter("userId", userId)
                                        .getResultList();


        return rst;

        /*
            select * from cart c
            join product p
            on c.product_id = p.product_id
            where user_id = #{userId}

         */

    }

    @Override
    public Cart addCart(Long productId, int quantity, Long userId) {

        Cart cart = new Cart();

        // 영속성 컨텍스트에서 참조만 가져옴
        Product productRef = em.getReference(Product.class, productId);
        User userRef = em.getReference(User.class, userId);

        cart.setProduct(productRef);
        cart.setUser(userRef);
        cart.setQuantity(quantity);
        cart.setPurchaseStatus(EnumPurchaseStatus.NOT_PURCHASED);

        em.persist(cart);

        return cart;

        /*
            insert into cart(
                            product_id,
                            user_id,
                            quantity,
                            purchase_status
                            ) valuses(
                                    #{productId},
                                    #{userId},
                                    #{quantity},
                                    "NOT_PURCHASED"
         */
    }

    @Override
    public boolean deleteCart(Long userId, Long productId) {

        int deletedCount = em.createQuery(
                        "DELETE FROM Cart c WHERE c.user.userId = :userId AND c.product.productId = :productId")
                .setParameter("userId", userId)
                .setParameter("productId", productId)
                .executeUpdate();



        return deletedCount > 0;



    /*
        delete from cart
        where user_id = #{userId}
        and product_id = #{productId}
    */
    }





}
