package com.revature.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantity;
    private double price;
    private String description;
    private String image;
    private String name;
    private String genre;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference(value = "cart_product")
    List<Cart> carts;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference(value = "wish_product")
    List<WishList> wishLists;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference(value = "order_product")
    List<OrderProduct> orders;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @JsonManagedReference(value = "review_product")
    List<Review> reviews;

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + id +
                "name=" + name +
                '}';
    }
    public Product(int id) {
        this.id = id;
    }
    public Product(int id, int quantity, double price, String description, String image, String name, String genre) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.image = image;
        this.name = name;
        this.genre = genre;
    }
}
