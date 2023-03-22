package com.revature.services;

import com.revature.dtos.ProductInfo;
import com.revature.models.Cart;
import com.revature.models.Product;
import com.revature.repositories.CartRepository;
import com.revature.repositories.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private static boolean ispresent;
    private static int cartId;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public ProductService(ProductRepository productRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll(Sort.by("id"));
    }

    public Optional<Product> findById(int id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }
    
    public List<Product> saveAll(List<Product> productList, List<ProductInfo> metadata) {
    	return productRepository.saveAll(productList);
    }

    public void delete(int id) {
        productRepository.deleteById(id);
    }

    public List<Product> findByGenre(String genre, Integer id) {
        List<Product> genreList = findAll();

        genreList.removeIf(n -> (!n.getGenre().equals(genre)));
        genreList.removeIf(n -> (n.getId() == id));

        Collections.shuffle(genreList);

        return genreList;
    }

    public List<Product> findByName(String name) {
        List<Product> productSearch = findAll();

        productSearch.removeIf(n -> (!n.getName().toLowerCase().contains(name)));

        return productSearch;
    }
    public Product findByProdId(Integer productId){
        return productRepository.findByProdId(productId);
    }

    //returns the list of carts associated with a user's id
    public List<Cart> getCart(int id){
        return cartRepository.getCart(id);
    }

    // adds a cart
    public Cart addCart(Cart cart){
        return cartRepository.save(cart);
    }

    // removes all carts associated with a user's id
    public void clearCart(Integer id){
        cartRepository.clearCart(id);
    }

    // checks to see if a cart is present
    public boolean inCart(Integer userId, Integer prodId){
        ispresent = false;
        cartRepository.findAll().forEach(element ->{
            if(element.getUser().getId() == userId && element.getProduct().getId() == prodId){
                ispresent = true;
            }
        });
        return ispresent;
    }

    // finds a cart's id
    public int findCart(Integer userId, Integer prodId){
        cartId = -1;
        cartRepository.findAll().forEach(element ->{
            if(element.getUser().getId() == userId && element.getProduct().getId() == prodId){
                cartId = element.getId();
            }
        });
        return cartId;
    }

    // adds a quantity to a given cart's quantity
    public Cart addQuanToCart(Integer cartId, Integer quantity){
        Cart c = cartRepository.findById(cartId).get();
        int currentQuantity = c.getQuantity();
        c.setQuantity(currentQuantity + quantity);
        return c;
    }

    // deletes a cart
    public void deleteCartProduct(Integer userId, Integer prodId){
        cartRepository.deleteCartProduct(userId, prodId);
    }
}
