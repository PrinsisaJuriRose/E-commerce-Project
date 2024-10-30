package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
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

public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepo = mock(UserRepository.class);
    private OrderRepository orderRepo = mock(OrderRepository.class);
    private User user;

    @Before
    public void setUp()
    {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository",userRepo);
        TestUtils.injectObjects(orderController, "orderRepository",orderRepo);

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
        Cart cart = new Cart();
        cart.setId(0L);
        cart.setItems(items);
        BigDecimal total = BigDecimal.valueOf(2.99);
        cart.setTotal(total);

        /*Create user*/
        user = new User();
        user.setId(0L);
        user.setUsername("test");
        user.setPassword("testPassword");

        user.setCart(cart);
        cart.setUser(user);

        when(userRepo.findByUsername("test")).thenReturn(user);
        when(userRepo.findByUsername("not_exit_user")).thenReturn(null);
    }

    @Test
    public void submit_happy_path() {

        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
        verify(userRepo, times(1)).findByUsername(user.getUsername());

    }

    @Test
    public void submit_user_not_found_bad() {
        ResponseEntity<UserOrder> response = orderController.submit("not_exit_user");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRepo, times(1)).findByUsername("not_exit_user");
    }

    @Test
    public void get_orders_happy_path() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("test");
        assertNotNull(ordersForUser);
        assertEquals(HttpStatus.OK, ordersForUser.getStatusCode());
        List<UserOrder> orders = ordersForUser.getBody();
        assertNotNull(orders);
        verify(userRepo, times(1)).findByUsername(user.getUsername());

    }

    @Test
    public void get_orders_user_not_found_bad() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("not_exit_user");
        assertNotNull(ordersForUser);
        assertEquals(HttpStatus.NOT_FOUND, ordersForUser.getStatusCode());
        verify(userRepo, times(1)).findByUsername("not_exit_user");

    }

}
