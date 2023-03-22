package com.revature.repositories;

import com.revature.models.Product;
import com.revature.models.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query(value = "SELECT * FROM reviews WHERE id = :reviewId", nativeQuery = true)
    Review findByReviewId(@Param("reviewId") Integer reviewId);

    @Query(value = "SELECT * FROM reviews WHERE user_id = :userId AND product_id = :productId", nativeQuery = true)
    List<Review> findByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    @Query(value = "SELECT * FROM reviews WHERE product_id = :productId", nativeQuery = true)
    List<Review> getAll(@Param("productId") Integer productId);

    @Query(value = "SELECT AVG(rating) FROM reviews WHERE product_id = :productId", nativeQuery = true)
    Double getAvg(@Param("productId") Integer productId);
}
