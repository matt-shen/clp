package com.revature.services;

import com.revature.dtos.CartDto;
import com.revature.dtos.ProductInfo;
import com.revature.models.Cart;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.repositories.CartRepository;
import com.revature.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTests {
    public ProductService sut;

    @Mock
    private Product mockProduct;

    @Mock
    private List<Product> mockProductList;

    @Mock
    private List<ProductInfo> mockProductInfoList;

    @Mock
    ProductRepository mockProductRepository;

    @Mock
    CartRepository mockCartRepository;

    private final Integer id = 1;

    @Test
    void findAllTest() {

        sut = new ProductService(mockProductRepository, null);
        Mockito.when(mockProductRepository.findAll(Sort.by("id"))).thenReturn(mockProductList);
        Optional<List<Product>> products = Optional.of(sut.findAll());
        Assertions.assertEquals(Optional.of(mockProductList), products);
    }

    @Test
    void findByIdTest() {

        sut = new ProductService(mockProductRepository, null);
        Mockito.when(mockProductRepository.findById(id)).thenReturn(Optional.of(mockProduct));
        Optional<Product> product = sut.findById(id);
        Assertions.assertEquals(Optional.of(mockProduct), product);
    }

    @Test
    void saveTest() {
        sut = new ProductService(mockProductRepository, null);
        Mockito.when(mockProductRepository.save(mockProduct)).thenReturn(mockProduct);
        Optional<Product> product = Optional.of(sut.save(mockProduct));
        Assertions.assertEquals(Optional.of(mockProduct), product);
    }

    @Test
    void saveAllTest() {

        sut = new ProductService(mockProductRepository, null);
    Mockito.when(mockProductRepository.saveAll(mockProductList)).thenReturn(mockProductList);
        Optional<List<Product>> products = Optional.of(sut.saveAll(mockProductList, mockProductInfoList));
        Assertions.assertEquals(Optional.of(mockProductList), products);
    }

    @Test
    void deleteTest() {

        sut = new ProductService(mockProductRepository, null);
        sut.delete(id);
        verify(mockProductRepository).deleteById(id);
    }

    @Test
    void getCartTest(@Mock CartRepository mockCartRepository, @Mock ProductRepository mockProductRepository){
        sut = new ProductService(mockProductRepository, mockCartRepository);
        List<Cart> cartList = new LinkedList<Cart>();
        Cart cart = new Cart(id, new User(id), new Product (id), 4);
        cartList.add(cart);
        Mockito.when(mockCartRepository.getCart(id)).thenReturn(cartList);
        List<Cart> carts = sut.getCart(id);
        Assertions.assertEquals(cartList, carts);
    }

    @Test
    void addCartTest(@Mock CartRepository mockCartRepository, @Mock ProductRepository mockProductRepository){
        Cart cart = new Cart(id, new User(id), new Product (id), 4);
        sut = new ProductService(mockProductRepository, mockCartRepository);
        Mockito.when(mockCartRepository.save(cart)).thenReturn(cart);
        Optional <Cart> c = Optional.of(sut.addCart(cart));
        Assertions.assertEquals(Optional.of(cart), c);
    }

    @Test
    void clearCartTest(@Mock CartRepository mockCartRepository, @Mock ProductRepository mockProductRepository){
        sut = new ProductService(mockProductRepository, mockCartRepository);
        sut.clearCart(id);
        verify(mockCartRepository).clearCart(id);
    }

    @Test
    void inCartTest(@Mock CartRepository mockCartRepository, @Mock ProductRepository mockProductRepository){
        sut = new ProductService(mockProductRepository, mockCartRepository);
        List<Cart> cartList = new LinkedList<Cart>();
        Cart cart = new Cart(id, new User(id), new Product (id), 4);
        cartList.add(cart);
        Mockito.when(mockCartRepository.findAll()).thenReturn(cartList);
        boolean b = sut.inCart(id, id);
        Assertions.assertEquals(true, b);
    }

    @Test
    void findCartTest(@Mock CartRepository mockCartRepository, @Mock ProductRepository mockProductRepository){
        sut = new ProductService(mockProductRepository, mockCartRepository);
        List<Cart> cartList = new LinkedList<Cart>();
        Cart cart = new Cart(id, new User(id), new Product (id), 4);
        cartList.add(cart);
        Mockito.when(mockCartRepository.findAll()).thenReturn(cartList);
        int cId = sut.findCart(id, id);
        Assertions.assertEquals(1, cId);
    }

    @Test
    void addQuanCartTest(@Mock CartRepository mockCartRepository, @Mock ProductRepository mockProductRepository){
        Cart cart = new Cart(1, new User(1), new Product (1), 4);
        Mockito.when(mockCartRepository.findById(id)).thenReturn(Optional.of(cart));
        sut = new ProductService(mockProductRepository, mockCartRepository);
        int quan = 2;
        sut.addQuanToCart(id, quan);
        Assertions.assertEquals(6, cart.getQuantity());
    }

    @Test
    void deleteCartProdTest(@Mock CartRepository mockCartRepository, @Mock ProductRepository mockProductRepository){
        sut = new ProductService(mockProductRepository, mockCartRepository);
        sut.deleteCartProduct(id, id);
        verify(mockCartRepository).deleteCartProduct(id, id);
    }

    @Test
    void findByGenreTest() {
        String genre = "test";
        sut = new ProductService(mockProductRepository, null);
        Mockito.when(mockProductRepository.findAll(Sort.by("id"))).thenReturn(mockProductList);
        //Mockito.when(mockProductRepository.findProductsByGenre(genre, id)).thenReturn(mockProductList);
        List<Product> genreProducts = sut.findByGenre(genre, id);
        Assertions.assertEquals(mockProductList, genreProducts);
    }

    @Test
    void findByNameTest() {
        String name = "Headphones";
        sut = new ProductService(mockProductRepository, null);
        Mockito.when(mockProductRepository.findAll(Sort.by("id"))).thenReturn(mockProductList);
        //Mockito.when(mockProductRepository.findProductsByName(name)).thenReturn(mockProductList);
        List<Product> nameProducts = sut.findByName(name);
        Assertions.assertEquals(mockProductList, nameProducts);
    }
}
