package com.example.library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    void testBookCreation() {
        Book book = new Book("AA01", "The Hobbit", "9780547928227", 2, 2, 32.50);

        assertEquals("AA01", book.getId());
        assertEquals("The Hobbit", book.getTitle());
        assertEquals("9780547928227", book.getIsbn());
        assertEquals(2, book.getCopies());
        assertEquals(2, book.getAvailable());
        assertEquals(32.50, book.getPrice());
    }

    @Test
    void testSetStatus() {
        Book book = new Book("AA01", "Test", "9780547928227", 1, 1, 20.0);

        book.setStatus("Valid");

        assertEquals("Valid", book.getStatus());
    }

    @Test
    void testSetAvailable() {
        Book book = new Book("AA01", "Test", "9780547928227", 2, 2, 20.0);

        book.setAvailable(1);

        assertEquals(1, book.getAvailable());
    }
}