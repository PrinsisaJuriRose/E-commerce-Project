package com.example.demo.model.persistence;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ItemTest {

    Item item1, item2;

    @Before
    public void setUp()
    {
        item1 = new Item();
        item1.setId(0L);
        item1.setName("Round Widget");
        item1.setPrice(new BigDecimal("2.99"));
        item1.setDescription("A widget that is round");

        item2 = new Item();
        item2.setId(1L);
        item2.setName("Square Widget");
        item2.setPrice(new BigDecimal("1.99"));
        item2.setDescription("A widget that is square");
    }
    @Test
    public void equals_bad()
    {
        assertNotEquals(item1, null);
        assertNotEquals(item2, null);
        assertNotEquals(item1, item2);
        assertFalse(item1.equals(item2) && item2.equals(item1));
        assertNotEquals(item2.hashCode(), item1.hashCode());
    }

    @Test
    public void equals_happy() {
        item1.setId(0L);
        item2.setId(0L);
        assertTrue(item1.equals(item2) && item2.equals(item1));
        assertEquals(item2.hashCode(), item1.hashCode());
    }
}
