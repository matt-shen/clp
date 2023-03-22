package com.revature.controllers;

import com.revature.annotations.Authorized;
import com.revature.exceptions.EmailTakenException;
import com.revature.models.User;
import com.revature.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * This is the profile controller
 */
@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://p3-static-hosting.s3-website.us-east-2.amazonaws.com"}, allowCredentials = "true", exposedHeaders = "Authorization")
public class ProfileController {

    private final UserService userService;


    /**
     * Constructor for the ProfileController
     * @param userService handle communication between the ProfileController and the UserRepository
     */
    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }


    /**
     * http get method to retrieve the user object for display on the user profile
     * @param session the HttpSession object containing the information for the logged-in user
     * @return User object for the current logged-in user to populate the user profile
     */
    @Authorized
    @GetMapping
    public ResponseEntity<User> getUserInfo(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
        int userId = loggedInUser.getId();
        Optional<User> optional = userService.findById(userId);


        if (!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        // need to either update this or add more to get user orders, reviews, and wishlist
        return ResponseEntity.ok(optional.get());
    }


    /**
     * http post method to update the user information
     * @param session the HttpSession object containing the information for the logged-in user
     * @param user a User object containing the information required to update the associated user in persistence
     * @return User object reflecting the changes made to the associated user in persistence
     */
    @Authorized
    @PostMapping()
    public ResponseEntity<User> postUserInfo(HttpSession session, @RequestBody User user) {
        User loggedInUser = (User) session.getAttribute("user");
        int userId = loggedInUser.getId();
        try {
            user = userService.save(userId, user);
        } catch(EmailTakenException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }

}
