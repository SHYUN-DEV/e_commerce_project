package com.example.newecommerce.product.infrastructure;


import com.example.newecommerce.product.domain.Product;
import com.example.newecommerce.product.domain.ProductRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {


    @PersistenceContext
    private EntityManager em;


    @Override
    public List<Product> bestProductInquiry() {

        //주문 및 주문상세 조인하고  결제 상태가 success인 아이디별 판매수량 합산
        List<Product> bestProductList = em.createQuery("SELECT od.product " +
                        "FROM OrderDetail od " +
                        "JOIN od.order o " +
                        "WHERE o.orderStatus = 'COMPLETED' " +
                        "GROUP BY od.product " +
                        "ORDER BY SUM(od.quantity) DESC," +
                        "od.product.productId ASC",  Product.class)
                        .setMaxResults(5)
                        .getResultList();





        //SELECT
        //    od.product_id,
        //    od.product_name,
        //    SUM(od.quantity) AS total_quantity
        //RANK() OVER (ORDER BY SUM(od.quantity) DESC) AS ranking
        //FROM order_detail od
        //JOIN `order` o ON od.order_id = o.order_id
        //WHERE o.order_status = 'success'
        //GROUP BY od.product_id, od.product_name
        //ORDER BY total_quantity DESC, product_id ASC
        //LIMIT 5;


//        SELECT product_id, product_name, total_quantity
//        FROM (
//                SELECT
//                od.product_id,
//                od.product_name,
//                SUM(od.quantity) AS total_quantity,
//                RANK() OVER (ORDER BY SUM(od.quantity) DESC) AS ranking
//                FROM order_detail od
//                JOIN `order` o ON od.order_id = o.order_id
//                WHERE o.order_status = 'success'
//                GROUP BY od.product_id, od.product_name
//        ) AS ranked
//        WHERE ranking <= 5
//        ORDER BY total_quantity DESC;

        return bestProductList;
    }




    @Override
    public Product getProductInfo(Long orderDetailProductId) {
        Product productInfo = em.createQuery("select p from Product p " +
                                "where p.productId = :orderDetailProductId " +
                                "order by p.productId asc"
                                ,Product.class)
                                .setParameter("orderDetailProductId", orderDetailProductId)
                                .getSingleResult();

        return productInfo;


        /*
        select * from prodcut
        where prodcut_id = #{orderDetailProductId}
        order by productId asc

         */
    }



    @Override
    public int decreaseInventory(Long productId, int updateInventory) {

        int rst = em.createQuery("update Product p set p.inventory = :updateInventory " +
                        "where p.productId = :prodcutId")
                .setParameter("updateInventory", updateInventory)
                .setParameter("prodcutId", productId)
                .executeUpdate();

        return rst;


        /*
        update product
        set inventory = #{updateInventory}
        where product_id = #{productId}

         */
    }


}
