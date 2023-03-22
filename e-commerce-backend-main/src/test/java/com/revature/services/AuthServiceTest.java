package com.revature.services;

import com.revature.models.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    public AuthService sut;

    @Mock
    UserService userServiceMock;

    @BeforeAll
    public static void beforeAll() {
        System.out.println("Starting AuthService tests...");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("AuthService Tests complete.");
    }

    @BeforeEach
    public void beforeEach(){
        sut = new AuthService(userServiceMock);
    }

    @Test
    public void testAuthenticateUserWithCorrectCredentials(){
        //set up test data
        String email = "testemail@gmail.com";
        String password = "password";
        User returnedUser = new User(1, email, "encryptedPass", "first", "last");

        //mock the call to the user service
        Mockito.when(userServiceMock.getUser(email)).thenReturn(Optional.of(returnedUser));

        //Use static method mocking to mock the call to BCrypt
        try(MockedStatic<BCrypt> bcrypt = Mockito.mockStatic(BCrypt.class)){
            bcrypt.when(() -> BCrypt.checkpw(password, "encryptedPass")).thenReturn(true);

            //Run the test
            Optional<User> result = sut.authenticateUser(email, password);
            Assertions.assertEquals(Optional.of(returnedUser), result);
        }
    }

    @Test
    public void testAuthenticateUserWithIncorrectPassword(){
        //set up test data
        String email = "testemail@gmail.com";
        String password = "wrongPassword";
        User returnedUser = new User(1, email, "encryptedPass", "first", "last");

        //mock the call to the user service
        Mockito.when(userServiceMock.getUser(email)).thenReturn(Optional.of(returnedUser));

        //Use static method mocking to mock the call to BCrypt, return false
        try(MockedStatic<BCrypt> bcrypt = Mockito.mockStatic(BCrypt.class)){
            bcrypt.when(() -> BCrypt.checkpw(password, "encryptedPass")).thenReturn(false);

            //Run the test
            Optional<User> result = sut.authenticateUser(email, password);
            Assertions.assertEquals(Optional.empty(), result);
        }
    }

    @Test
    public void testAuthenticateUserWithIncorrectEmail(){
        //set up test data
        String email = "wrongemail@gmail.com";
        String password = "password";

        //mock the call to the user service, return nothing
        Mockito.when(userServiceMock.getUser(email)).thenReturn(Optional.empty());

        Optional<User> result = sut.authenticateUser(email, password);
        Assertions.assertEquals(Optional.empty(), result);
    }

    @Test
    public void testRegisterUser(){
        //set up test data
        User testUser = new User("test@gmail.com", "pass", "test", "user");
        User expected = new User(1, "test@gmail.com", "pass", "test", "user");

        //mock the call to the user service
        Mockito.when(userServiceMock.save(testUser)).thenReturn(expected);

        //run test
        User result = sut.register(testUser);
        Assertions.assertEquals(expected, result);
    }
}
