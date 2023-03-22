package com.revature.services;

import com.revature.models.Product;
import com.revature.models.WishList;
import com.revature.repositories.ProductRepository;
import com.revature.repositories.WishListRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;

    public WishListService(WishListRepository wishListRepository, ProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
    }

    // getWishList retrieves a list of WishList objects and converts them to Product objects before returning
    // the list of product objects
    public List<Product> getWishList(Integer userId) {
        List<WishList> wishList = wishListRepository.findUserWishList(userId);
        List<Product> wishProducts = new ArrayList<Product>();
        if (wishList.size() > 0) {
            for(WishList product: wishList) {
                wishProducts.add(product.getProduct());
            }
        }
        return wishProducts;
    }

    public void deleteWishListItem(int id) {
        wishListRepository.deleteById(id);
    }

    public int addWishListItem(int userId, int productId) {
        // Check if the user's wishlist already contains the item. If not, add it.
        List<Product> wishList = getWishList(userId);
        Product itemToAdd = productRepository.findById(productId).get();
        if (wishList.contains(itemToAdd)) {
            return productId;
        }
        return wishListRepository.addWishListItem(userId, productId);
    }

    public Boolean checkIfWishListed(int userId, int productId) {
        if (wishListRepository.findByUserAndProduct(userId, productId).isPresent()) {
            return true;
        }
        return false;
    }

    public Optional<WishList> findById(int id) {
        return wishListRepository.findById(id);
    }

    public Optional<WishList> findByUserAndProduct(int userId, int productId) {
        return wishListRepository.findByUserAndProduct(userId, productId);
    }

}
