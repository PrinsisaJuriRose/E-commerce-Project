package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);
    private Cart cart;
    private ModifyCartRequest r;

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);

        /*Create item list*/
        Item item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        List<Item> items = new ArrayList<Item>();
        items.add(item);

        /*Create cart*/
        cart = new Cart();
        cart.setId(0L);
        cart.setItems(items);
        BigDecimal total = BigDecimal.valueOf(2.99);
        cart.setTotal(total);

        /*Create user*/
        User user = new User();
        user.setId(0L);
        user.setUsername("test");
        user.setPassword("testPassword");

        user.setCart(cart);
        cart.setUser(user);

        r = new ModifyCartRequest();
        r.setItemId(item.getId());
        r.setQuantity(1);
        r.setUsername(user.getUsername());
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepo.findById(item.getId())).thenReturn(java.util.Optional.of(item));
    }

    @Test
    public void add_to_cart_happy_path() {

        ResponseEntity<Cart> response = cartController.addTocart(r);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Cart c = response.getBody();
        assertNotNull(c);
        assertEquals(cart.getTotal(), c.getTotal());
        assertEquals(cart.getUser().getUsername(), c.getUser().getUsername());
        assertEquals(cart.getId(), c.getId());

    }

    @Test
    public void add_to_cart_not_exit_user_bad() {
        r.setUsername("not_exit_user");
        ResponseEntity<Cart> response = cartController.addTocart(r);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void add_to_cart_not_exit_item_bad() {
        r.setItemId(2L);
        ResponseEntity<Cart> response = cartController.addTocart(r);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void remove_from_cart_happy_path() {

        ResponseEntity<Cart> response = cartController.addTocart(r);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = cartController.removeFromcart(r);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Cart c = response.getBody();
        assertNotNull(c);
        assertEquals(cart.getTotal(), c.getTotal());
        assertEquals(cart.getUser().getUsername(), c.getUser().getUsername());
        assertEquals(cart.getId(), c.getId());
    }

    @Test
    public void remove_from_cart_not_exit_user_bad() {
        r.setUsername("not_exit_user");
        ResponseEntity<Cart> response = cartController.removeFromcart(r);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void remove_from_cart_not_exit_item_bad() {
        r.setItemId(2L);
        ResponseEntity<Cart> response = cartController.removeFromcart(r);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
