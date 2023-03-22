package com.revature.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/*
 * Database model for the cart
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "cart_user")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @JsonBackReference(value = "cart_product")
    private Product product;

    private Integer quantity;

    public Integer getId(){
        return this.id;
    }

    public Integer getUserId(){
        return user.getId();
    }

    public Integer getProductId(){
        return product.getId();
    }

    public Integer getQuantity(){
        return this.quantity;
    }

    public User getUser(){
        return this.user;
    }
    
    public Product getProduct(){
        return this.product;
    }

    @Override
    public String toString(){
        return "Cart{" +
                "cartId=" + id +
                "user=" + user.toString() +
                "product=" + product.toString() +
                "quantity=" + quantity +
                '}';
    }
}
