package com.revature.repositories;

import com.revature.models.Order;
import com.revature.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    //find all orders belonging to a user
    public List<Order> findByUserId(Integer userId);

    /**
     * Retrieve the 5 most recent orders for a user ordered from most recent
     * @param userId the userId that the orders are being retrieved for
     * @return List of Order objects containing the 5 most recent orders for a user
     */
    @Query(value = "SELECT * FROM orders WHERE user_id = :userId ORDER BY date_ordered DESC LIMIT 5;", nativeQuery = true)
    public List<Order> findByUserIdWithLimit(@Param("userId") Integer userId);

    //find a specific order belonging to a user
    public Order findByOrderIdAndUserId(Integer orderId, Integer userId);
}
