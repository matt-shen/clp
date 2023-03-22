package com.revature.controllers;

import com.revature.annotations.Authorized;
import com.revature.dtos.OrderDto;
import com.revature.models.Product;
import com.revature.models.Review;
import com.revature.models.User;
import com.revature.services.ProductService;
import com.revature.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import com.revature.annotations.Authorized;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/review")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://p3-static-hosting.s3-website.us-east-2.amazonaws.com"}, allowCredentials = "true", exposedHeaders = "Authorization")
@Transactional
public class ReviewController {
    private final ReviewService reviewService;
    private final ProductService productService;

    @Autowired
    public ReviewController(ReviewService reviewService, ProductService productService) {
        this.reviewService = reviewService;
        this.productService = productService;
    }

    @PostMapping(value = "/add")
    @Authorized
    public ResponseEntity<Review> postReview(HttpSession session, @RequestBody Review review) {
        review.setUser((User)session.getAttribute("user"));
        return ResponseEntity.ok(reviewService.saveReview(review));
    }

    @GetMapping(value = "/{productId}")
    @Authorized
    public List<Review> hasReviewed(HttpSession session, @PathVariable("productId") int productId) {
        User user = (User)session.getAttribute("user");
        // Product product = productService.findByProdId(productId);
        //System.out.println("In hasReviewed method of review controller: " + user.toString() + " and " + product.toString());
        List<Review> temporary = reviewService.getReview(user.getId(), productId);
        return temporary;
    }

    @GetMapping(value = "/ping")
    //@Authorized
    public String ping(HttpSession session) {
        return "Pong";
    }

    @GetMapping(value = "/all/{productId}")
    public ResponseEntity<List<Review>> getAllReviews(HttpSession session, @PathVariable("productId") int productId) {
        List<Review> temporary = reviewService.getAll(productId);
        return ResponseEntity.ok(temporary);
    }

    @GetMapping(value = "/avg/{productId}")
    //@Authorized
    public Double avgReview(HttpSession session, @PathVariable("productId") int productId) {
        Double temp = Math.floor(reviewService.getAverage(productId)*10)/10;
        if( temp == null) temp = 0.0;
        return temp;
    }

}
