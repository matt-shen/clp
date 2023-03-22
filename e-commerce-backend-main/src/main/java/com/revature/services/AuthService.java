package com.revature.services;

import com.revature.models.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> authenticateUser(String email, String password){
        Optional<User> user = userService.getUser(email);
        if(user.isPresent() && BCrypt.checkpw(password, user.get().getPassword())){
            return user;
        }else{
            return Optional.empty();
        }
    }

    public User register(User user) {
        return userService.save(user);
    }
}
