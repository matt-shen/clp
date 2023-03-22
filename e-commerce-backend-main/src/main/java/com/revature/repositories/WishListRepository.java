package com.revature.repositories;

import com.revature.models.Order;
import com.revature.models.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Integer> {

    @Query(value = "SELECT * FROM wish_list WHERE user_id = :userId", nativeQuery = true)
    List<WishList> findUserWishList(@Param("userId") Integer userId);

    @Query(value = "SELECT * FROM wish_list WHERE user_id = :userId AND product_id = :productId", nativeQuery = true)
    Optional<WishList> findByUserAndProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO wish_list (user_id, product_id) VALUES (:userId, :productId)", nativeQuery = true)
    int addWishListItem(@Param("userId") Integer userId, @Param("productId") Integer productId);
}
