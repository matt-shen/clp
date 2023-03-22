package com.revature.services;

import com.revature.exceptions.EmailTakenException;
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    public UserService sut;

    @Mock
    private User mockUser;

    @Mock
    private User mockUserByEmail = new User(55,"email", "password", "firstname", "lastname");

    // Strings are final and cannot be mocked
    private String email = "mock@email.com";

    private String password = "password";

    @Mock
    UserRepository mockUserRepository;

    // set up the mocked user repository
    @BeforeAll
    public static void beforeAll() {
        System.out.println("Starting UserService tests...");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("UserService Tests complete.");
    }

    @Test
    public void testFindByEmailCorrectCredentialsProvided() {
        sut = new UserService(mockUserRepository);
        Mockito.when(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        Optional<User> user = sut.getUser(email);

        Assertions.assertEquals(Optional.of(mockUser), user);
    }

    @Test
    public void testFindByEmailIncorrectCredentialsProvided() {
        sut = new UserService(mockUserRepository);
        Mockito.when(mockUserRepository.findByEmail(email)).thenReturn(Optional.empty());
        Optional<User> user = sut.getUser(email);

        Assertions.assertEquals(Optional.empty(), user);
    }


    @Test
    public void testSaveUserUserObjectArgument() {
        sut = new UserService(mockUserRepository);
        Mockito.when(mockUserRepository.save(mockUser)).thenReturn(mockUser);
        User user = sut.save(mockUser);

        Assertions.assertEquals(mockUser, user);
    }

    @Test
    public void testSaveUserIntAndUserObjectOverride() {
        sut = new UserService(mockUserRepository);
        Mockito.when(mockUserRepository.save(mockUser)).thenReturn(mockUser);
        Mockito.when(sut.findById(1)).thenReturn(Optional.of(mockUser));
        Mockito.when(mockUserRepository.save(mockUser)).thenReturn(mockUser);

        User user = sut.save(1, mockUser);

        Assertions.assertEquals(mockUser, user);
    }

    @Test
    public void testSaveUserIntAndUserObjectOverrideException() {
        sut = new UserService(mockUserRepository);
        Mockito.when(mockUser.getEmail()).thenReturn("mockEmail");
        Mockito.when(mockUserByEmail.getId()).thenReturn(2);
        Mockito.when(mockUserRepository.findByEmail(mockUser.getEmail())).thenReturn(Optional.of(mockUserByEmail));

        System.out.println(mockUserByEmail.getId());
        System.out.println(mockUser.getId());
        Mockito.when(sut.findById(1)).thenReturn(Optional.of(mockUser));

        Assertions.assertThrows(
                EmailTakenException.class,
                () -> sut.save(1, mockUser)
        );
    }

    @Test
    public void testFindByIntWhereUserIdInPersistence() {
        sut = new UserService(mockUserRepository);
        Mockito.when(mockUserRepository.findById(1)).thenReturn(Optional.of(mockUser));
        Optional<User> user = sut.findById(1);

        Assertions.assertEquals(Optional.of(mockUser), user);
    }

    @Test
    public void testFindByIntWhereUserIdNotInPersistence() {
        sut = new UserService(mockUserRepository);
        Optional<User> userNotFound = Optional.empty();
        Mockito.when(mockUserRepository.findById(1)).thenReturn(Optional.empty());
        Optional<User> user = sut.findById(1);

        Assertions.assertEquals(Optional.empty(), user);
    }




}
