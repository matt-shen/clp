package com.revature.controllers;

import com.revature.annotations.Authorized;
import com.revature.dtos.LoginRequest;
import com.revature.models.User;
import com.revature.models.WishList;
import com.revature.services.ProductService;
import com.revature.models.Product;
import com.revature.services.WishListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

// NOTE: the term "WishList" is used inconsistently. "WishList" is used to refer to either a wishlisted item
// (a row in the wish_list table) OR to a list of wishlisted products (which is returned to the front-end).
// Check return values to see which definition is being used for a given function.

@RestController
@RequestMapping("/wishlist")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://p3-static-hosting.s3-website.us-east-2.amazonaws.com"}, allowCredentials = "true", exposedHeaders = "Authorization")
public class WishListController {

    private final WishListService wishListService;
    private final ProductService productService;

    public WishListController(WishListService wishListService, ProductService productService) {
        this.wishListService = wishListService;
        this.productService = productService;
    }

    // getWishList returns a list of wishlisted products
    @Authorized
    @GetMapping
    public ResponseEntity<List<Product>> getWishList(HttpSession session) {
        User user = (User) session.getAttribute("user");
        int userId = user.getId();
        return ResponseEntity.ok(wishListService.getWishList(userId));
    }

    @Authorized
    @GetMapping("/{product_id}")
    public ResponseEntity<Boolean> checkIfWishListed(@PathVariable("product_id") int productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        int userId = user.getId();
        return ResponseEntity.ok(wishListService.checkIfWishListed(userId, productId));
    }


    // addWishListItem adds a WishList item to the wish_list table
    @Authorized
    @PostMapping("/{product_id}")
    public ResponseEntity<Integer> addWishListItem(@PathVariable("product_id") int productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        int userId = user.getId();
        return ResponseEntity.ok(wishListService.addWishListItem(userId, productId));
    }

    @Authorized
    @DeleteMapping("/{product_id}")
    public ResponseEntity<WishList> deleteWishListItem(@PathVariable("product_id") int productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        int userId = user.getId();

        Optional<WishList> optional = wishListService.findByUserAndProduct(userId, productId);

        if(!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        wishListService.deleteWishListItem(optional.get().getId());

        return ResponseEntity.ok(optional.get());
    }
}
