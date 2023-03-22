package com.revature.repositories;

import com.revature.models.Cart;
import com.revature.models.Order;
import com.revature.models.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import javax.transaction.Transactional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    
    /*
     * retrives the carts given a user id
     * @param userId
     * @return list of carts
     */
    @Query(value = "SELECT * FROM cart WHERE user_id = :userId", nativeQuery = true)
    List<Cart> getCart(
        @Param("userId") Integer userId
    );
    
    /*
     * clears a cart
     * @param userId
     */
    @Modifying
    @Query(value = "DELETE FROM cart WHERE user_id = :userId", nativeQuery = true)
    void clearCart(@Param("userId") Integer userId);
    
    /*
     * deletes a specific cart
     * @param userId, productId
     */
    @Modifying
    @Query(value = "DELETE FROM cart WHERE user_id = :userId AND product_id = :prodId", nativeQuery = true)
    void deleteCartProduct(@Param("userId") Integer userId, @Param("prodId") Integer prodId);
}
