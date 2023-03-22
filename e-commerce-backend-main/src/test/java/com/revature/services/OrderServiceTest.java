package com.revature.services;

import com.revature.dtos.OrderDto;
import com.revature.dtos.OrderProductDto;
import com.revature.exceptions.InvalidOrderException;
import com.revature.models.Order;
import com.revature.models.OrderProduct;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.repositories.OrderRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


    /*
    the 3 A's of unit testing:
     - Arrange - set up the parameters for valid testing
     - Act - perform the test
     - Assert - verify the outcome of the action

     @Test
     @BeforeEach
     @AfterEach
     @AfterAll
     @BeforeAll
     */


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    public OrderService sut;

    @Mock
    OrderRepository repoMock;

    @BeforeAll
    public static void beforeAll() {
        System.out.println("Starting OrderService tests...");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("OrderService Tests complete.");
    }

    @BeforeEach
    public void beforeEach(){
        sut = new OrderService(repoMock);
    }


    @Test
    public void testCreateOrderWithValidOrder(@Mock OrderDto order, @Mock Order newOrder){
        //order has a product
        List<OrderProductDto> products = new ArrayList<>();
        products.add(new OrderProductDto(new Product(1), 1));
        Mockito.when(order.getProducts()).thenReturn(products);
        //order converted into right datatype
        Mockito.when(order.toOrder()).thenReturn(newOrder);

        sut.createOrder(order);

        verify(repoMock).save(ArgumentMatchers.eq(newOrder));

    }

    @Test
    public void testCreateOrderWithInvalidOrderNoProducts(@Mock OrderDto order){
        //order has no products
        List<OrderProductDto> products = new ArrayList<>();
        Mockito.when(order.getProducts()).thenReturn(products);

        //won't accept order with no products
        InvalidOrderException exc = Assertions.assertThrows(InvalidOrderException.class, ()->{sut.createOrder(order);});

        Assertions.assertEquals(exc.getMessage(), "Invalid Order: no products");
    }

    @Test
    public void testGetOrdersValidUserId() throws Exception {
        //create order to be returned by the repo
        LocalDateTime now = LocalDateTime.now();
        Order repoOrder = new Order(1, now, new User(1), new ArrayList<OrderProduct>());
        List<Order> repoOrders = new ArrayList<>();
        repoOrders.add(repoOrder);

        Mockito.when(repoMock.findByUserId(1)).thenReturn(repoOrders);

        //make sure the service converts the order into an OrderDto
        OrderDto order = new OrderDto(1, now, 1, new ArrayList<OrderProductDto>());
        List<OrderDto> expectedOrders = new ArrayList<>();
        expectedOrders.add(order);

        List<OrderDto> result = sut.getOrders(1);

        //ensure the converted order is equal to the expected order
        Assertions.assertEquals(expectedOrders, result);
    }

    @Test
    public void testGetOrdersForProfile() {
        LocalDateTime now = LocalDateTime.now();
        List<Order> profileOrders = new ArrayList<>();
        profileOrders.add(new Order(1, now, new User(1), new ArrayList<OrderProduct>()));
        Mockito.when(repoMock.findByUserIdWithLimit(1)).thenReturn(profileOrders);

        List<OrderDto> result = sut.getOrdersForProfile(1);

        OrderDto order = new OrderDto(1, now, 1, new ArrayList<OrderProductDto>());
        List<OrderDto> expectedOrders = new ArrayList<>();
        expectedOrders.add(order);

        Assertions.assertEquals(expectedOrders, result);
    }

    @Test
    public void testGetUserValidOrderId(){
        //mock response from the repo
        LocalDateTime now = LocalDateTime.now();
        Order repoOrder = new Order(3, now, new User(1), new ArrayList<OrderProduct>());

        Mockito.when(repoMock.findByOrderIdAndUserId(3, 1)).thenReturn(repoOrder);

        //expect to return an Order converted into OrderDto
        OrderDto expectedOrder = new OrderDto(3, now, 1, new ArrayList<OrderProductDto>());

        //test
        OrderDto result = sut.getOrder(3, 1);

        //assert
        Assertions.assertEquals(expectedOrder, result);
    }

    @Test
    public void testGetUserInvalidOrder(){
        //mock response from the repo, no order found
        Mockito.when(repoMock.findByOrderIdAndUserId(5, 1)).thenReturn(null);

        InvalidOrderException ex = Assertions.assertThrows(InvalidOrderException.class, ()->{sut.getOrder(5, 1);});

        Assertions.assertEquals("No order with that ID found for current user", ex.getMessage());
    }

}
