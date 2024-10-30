package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepo = mock(ItemRepository.class);
    private Item item;


    @Before
    public void setUp()
    {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository",itemRepo);

        item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        when(itemRepo.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepo.findById(0L)).thenReturn(java.util.Optional.of(item));
        when(itemRepo.findByName("Round Widget")).thenReturn(Collections.singletonList(item));
    }

    @Test
    public void get_all_items_happy_path() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item.getId(), items.get(0).getId());
        assertEquals(item.getName(), items.get(0).getName());
        assertEquals(item.getPrice(), items.get(0).getPrice());
        assertEquals(item.getDescription(), items.get(0).getDescription());
        verify(itemRepo, times(1)).findAll();
    }

    @Test
    public void get_item_by_id_happy_path() {
        ResponseEntity<Item> response = itemController.getItemById(item.getId());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Item t = response.getBody();
        assertNotNull(t);
        assertEquals(item.getId(), t.getId());
        assertEquals(item.getName(), t.getName());
        assertEquals(item.getPrice(), t.getPrice());
        assertEquals(item.getDescription(), t.getDescription());
        verify(itemRepo, times(1)).findById(item.getId());
    }

    @Test
    public void get_item_by_id_not_found_bad() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(itemRepo, times(1)).findById(1L);
    }

    @Test
    public void get_items_by_name_happy_path() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName(item.getName());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item.getId(), items.get(0).getId());
        assertEquals(item.getName(), items.get(0).getName());
        assertEquals(item.getPrice(), items.get(0).getPrice());
        assertEquals(item.getDescription(), items.get(0).getDescription());
        verify(itemRepo, times(1)).findByName(item.getName());
    }

    @Test
    public void get_items_by_name_not_found_bad() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Widget");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(itemRepo, times(1)).findByName("Widget");
    }


}
