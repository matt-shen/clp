package com.revature.services;

import com.revature.exceptions.NotReviewedException;
import com.revature.models.Product;
import com.revature.models.Review;
import com.revature.models.User;
import com.revature.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) { this.reviewRepository = reviewRepository;}
    public List<Review> getAll(Integer productId) {  return reviewRepository.getAll(productId);}

    public Double getAverage(Integer productId) {
        Double review = reviewRepository.getAvg(productId);
        if(review == null) {
            return 0.0;
        } else {
            return review;
        }
    }

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getReview(Integer userId, Integer productId){
        List<Review> review = reviewRepository.findByUserIdAndProductId(userId, productId);

        if(review == null) throw new NotReviewedException("Product hasn't been reviewed by customer, yet!");
        else {
            return review;
        }
    }


}
