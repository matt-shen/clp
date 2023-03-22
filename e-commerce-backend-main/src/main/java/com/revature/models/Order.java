package com.revature.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;
    private LocalDateTime dateOrdered;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonBackReference(value = "order_user")
    @JoinColumn(name = "user_id")
    @JsonProperty
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "order_order_product")
    List<OrderProduct> orderProducts;

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", dateOrdered=" + dateOrdered +
                ", user=" + user +
                '}';
    }
}
