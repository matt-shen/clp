package com.revature.controllers;

import com.revature.annotations.Authorized;
import com.revature.dtos.CartDto;
import com.revature.dtos.ProductInfo;
import com.revature.models.Cart;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.services.ProductService;
import com.revature.services.UserService;

import org.springframework.boot.context.config.UnsupportedConfigDataLocationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

/**
 * This is the controller for handling requests related to products using the "/api/product" route
 */
@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://p3-static-hosting.s3-website.us-east-2.amazonaws.com"}, allowCredentials = "true", exposedHeaders = "Authorization")
@Transactional
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    /**
     * This method returns a completel list of all products in the database
     * @return List of products in the database
     */
    @Authorized
    @GetMapping
    public ResponseEntity<List<Product>> getInventory() {
        return ResponseEntity.ok(productService.findAll());
    }

    /**
     * This method returns a single product that contains the mapped
     * variable "id" at the end of the "/api/products/{id}" route
     * @param id
     * @return A product that contains "id"
     */
    @Authorized
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") int id) {
        Optional<Product> optional = productService.findById(id);

        if(!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optional.get());
    }

    @Authorized
    @PutMapping
    public ResponseEntity<Product> upsert(@RequestBody Product product) {
        return ResponseEntity.ok(productService.save(product));
    }

    /*
     * checkouts and purchases a list of products
     */
    @Authorized
    @PatchMapping
    public ResponseEntity<List<Product>> purchase(@RequestBody List<ProductInfo> metadata) { 	
    	List<Product> productList = new ArrayList<Product>();
    	
    	for (int i = 0; i < metadata.size(); i++) {
    		Optional<Product> optional = productService.findById(metadata.get(i).getId());

    		if(!optional.isPresent()) {
    			return ResponseEntity.notFound().build();
    		}

    		Product product = optional.get();

    		if(product.getQuantity() - metadata.get(i).getQuantity() < 0) {
    			return ResponseEntity.badRequest().build();
    		}
    		
    		product.setQuantity(product.getQuantity() - metadata.get(i).getQuantity());
    		productList.add(product);
    	}
        
        productService.saveAll(productList, metadata);
        return ResponseEntity.ok(productList);
    }

    /*
     * deletes a product based on the id
     * @param product id
    */
    @Authorized
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") int id) {
        Optional<Product> optional = productService.findById(id);

        if(!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        productService.delete(id);

        return ResponseEntity.ok(optional.get());
    }

    // retrieves the list of carts for the associated userId
    // @param user id
    // @return list of carts
    @Authorized
    @GetMapping("/cart")
    public List<Cart> getCart(@RequestParam int userId) {
        return productService.getCart(userId);
    }

    /*
     * Posts a new cart if that cart is not already present. 
     * If it is present, it simply increases the quantity of the present cart.
     * @param product id and quantity
     * @return added cart
     */
    @Authorized
    @PostMapping("/cart")
    public Cart addCart(@RequestParam int userId, @RequestParam int prodId, @RequestParam int quantity){
        boolean ispresent = productService.inCart(userId, prodId);
        if(ispresent == false){
            CartDto cart = new CartDto(userId, prodId, quantity, userService, productService);
            User newUser = cart.getUser();
            Product newProduct = cart.getProduct();
            Cart temp = new Cart(null, newUser, newProduct, quantity);
            Cart newCart = productService.addCart(temp);
            return newCart;
        }
        int cartId = productService.findCart(userId, prodId);
        return productService.addQuanToCart(cartId, quantity);
    }

    /*
     * Clears the cart
     * @param userid
     */
    @Authorized
    @DeleteMapping("/cart")
    public void clearCart(@RequestParam("userId") int userId){
        productService.clearCart(userId);
    }

    /*
     * returns the product associated with the product id
     * @param productid
     */
    @Authorized
    @GetMapping("/cart/{id}")
    public ResponseEntity<Product> getCartProductById(@PathVariable("id") int id) {
        Optional<Product> optional = productService.findById(id);

        if(!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optional.get());
    }

    /*
     * delets a single cart item
     * @param userid and productid
     */
    @Authorized
    @DeleteMapping("/cart/{id}")
    public void deleteCartProduct(@PathVariable("id") int userId, @RequestParam("prodId") int prodId){
        productService.deleteCartProduct(userId, prodId);
    }

    /*
     * returns the user id using http session
     * @param http session
     * @return userid
     */
    @Authorized
    @GetMapping("/cart/user")
    public int getUserId(HttpSession session){
        User loggedInUser = (User) session.getAttribute("user");
        int userId = loggedInUser.getId();
        return userId;
    }

    /**
     * This method returns a list of products that contain the mapped
     * variables "genre" request param "/api/products/genre" route, the
     * list will exclude a product with the matching "id"
     * @param genre
     * @param id
     * @return List of products that share the "genre" excluding any with a matching "id"
     */
    @Authorized
    @GetMapping(value = "/genre")
    public ResponseEntity<List<Product>> getProductByGenre(@RequestParam String genre, Integer id) {
        return ResponseEntity.ok(productService.findByGenre(genre, id));
    }

    /**
     * This method returns a list of products that contain
     * @param name this will be what the user searched
     * @return List of products that contain any part of the "name" variable
     */
    @Authorized
    @GetMapping(value = "/search")
    public ResponseEntity<List<Product>> getProductByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.findByName(name.toLowerCase()));
    }
}
