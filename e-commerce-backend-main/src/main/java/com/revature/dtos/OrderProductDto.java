package com.revature.dtos;

import com.revature.models.Product;

import java.util.Objects;

// This class is used to store Order Products in a more convenient way for the front-end
// Only has info on the product and quantity of that product
public class OrderProductDto {
    private Product product;
    private Integer quantity;

    public OrderProductDto(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProductDto that = (OrderProductDto) o;
        return Objects.equals(product, that.product) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity);
    }
}
