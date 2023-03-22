package com.revature.services;

import com.revature.dtos.ProductInfo;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.repositories.ProductRepository;
import com.revature.repositories.WishListRepository;
import com.revature.repositories.ProductRepository;

import com.revature.services.WishListService;
import com.revature.models.WishList;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WishListServiceTests {

  public WishListService sut;

  @Mock
  private Product mockProduct;

  @Mock
  private List<Product> mockProductList;

  @Mock
  private WishList mockWishListItem;

  @Mock
  private List<WishList> mockWishList;

  @Mock
  private List<ProductInfo> mockProductInfoList;

  @Mock
  WishListRepository mockWishListRepository;

  @Mock
  ProductRepository mockProductRepository;

  private final Integer id = 1;

  @Test
  void getWishListTest() {
      sut = new WishListService(mockWishListRepository, mockProductRepository);

      //set up test data: fake products and user
      Product product1 = new Product(2, 50, 49.99, "test product 1", "testImage.jpg", "test1", "test");
      Product product2 = new Product(5, 34, 19.50, "test product 2", "testImage2.jpg", "test2", "test");
      User testUser = new User(1);

      //create wish list to be returned by mocked repo
      WishList wishListItem1 = new WishList(1, testUser, product1);
      WishList wishListItem2 = new WishList(2, testUser, product2);
      List<WishList> returnedWishList = new ArrayList<>();
      returnedWishList.add(wishListItem1);
      returnedWishList.add(wishListItem2);

      //return the wish list when mocked repo's method is called
      Mockito.when(mockWishListRepository.findUserWishList(id)).thenReturn(returnedWishList);

      //create list of products expected to be returned by method
      List<Product> expected = new ArrayList<>();
      expected.add(product1);
      expected.add(product2);

      //test and compare result
      Optional<List<Product>> products = Optional.of(sut.getWishList(id));
      Assertions.assertEquals(Optional.of(expected), products);
  }

  @Test
  void findByIdTest() {
      sut = new WishListService(mockWishListRepository, mockProductRepository);
      Mockito.when(mockWishListRepository.findById(id)).thenReturn(Optional.of(mockWishListItem));
      Optional<WishList> wishList = sut.findById(id);
      Assertions.assertEquals(Optional.of(mockWishListItem), wishList);
  }

  @Test
  void findByUserAndProductTest() {
      sut = new WishListService(mockWishListRepository, mockProductRepository);
      Mockito.when(mockWishListRepository.findByUserAndProduct(id, id)).thenReturn(Optional.of(mockWishListItem));
      Optional<WishList> wishListItem = sut.findByUserAndProduct(id, id);
      Assertions.assertEquals(Optional.of(mockWishListItem), wishListItem);
  }

  @Test
  void deleteWishListItemTest() {
      sut = new WishListService(mockWishListRepository, mockProductRepository);
      sut.deleteWishListItem(id);
      verify(mockWishListRepository).deleteById(id);
  }

}
