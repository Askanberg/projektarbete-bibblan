package org.bibblan.bookcatalog;


import org.bibblan.bookcatalog.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemFactoryTests {
    private ItemFactory itemFactory;
    private BufferedReader mockBufferedReader;

    @BeforeEach
    public void setup() {
        itemFactory = new ItemFactory();
        mockBufferedReader = mock(BufferedReader.class);
    }

    @Test
    void testCreateBookFromValues() {
        String[] bookValues = {"Educated", "Tara Westover", "Biography", "Random House", "9780399590504", "Pocket" };

        Item item = itemFactory.createItem(bookValues, "Books");

        Book expectedBook = new Book(
                "Educated",
                new Author("Tara Westover", new ArrayList<>()),
                "Biography",
                "Random House",
                "9780399590504",
                CoverType.POCKET);

        assertEquals(expectedBook, item);
    }
    @Test
    void testCreateEBookFromValues() {
        String[] ebookValues = {"Effective Java", "Joshua Bloch", "Programming", "Addison-Wesley", "http://example.com/effective_java", "EPUB" };

        Item item = itemFactory.createItem(ebookValues, "EBooks");

        EBook expectedEbook = new EBook(
                "Effective Java",
                new Author("Joshua Bloch", new ArrayList<>()),
                "Programming",
                "Addison-Wesley",
                "http://example.com/effective_java",
                "EPUB");
        assertEquals(expectedEbook, item);
    }
    @Test
    void testCreateReferenceFromValues() {
        String[] ebookValues = {"The Pragmatic Programmer", "Andrew Hunt", "Programming", "Addison-Wesley", "9780135957059" };

        Item item = itemFactory.createItem(ebookValues, "References");

        Reference expectedReference = new Reference(
                "The Pragmatic Programmer",
                new Author("Andrew Hunt", new ArrayList<>()),
                "Programming",
                "Addison-Wesley",
                "9780135957059");

        assertEquals(expectedReference, item);
    }
    @Test
    void testThatBookIsAddedToAuthor() throws IOException {
        ItemFileReader itemFileReader = new ItemFileReader(itemFactory);
        itemFileReader.readItemsFromCsv("src/test/resources/testBooks.csv");
        Author author = itemFactory.getAuthorMap().get("Tara Westover");

        Book expectedBook = new Book(
                "Educated",
                new Author("Tara Westover", new ArrayList<>()),
                "Biography",
                "Random House",
                "9780399590504",
                CoverType.POCKET);

        assertEquals(expectedBook, author.getItems().get(0));
    }
    @Test
    void testThatEBookIsAddedToAuthor() throws IOException {
        ItemFileReader itemFileReader = new ItemFileReader(itemFactory);
        itemFileReader.readItemsFromCsv("src/test/resources/testEBooks.csv");
        Author author = itemFactory.getAuthorMap().get("Joshua Bloch");

        EBook expectedEBook = new EBook(
                "Effective Java",
                new Author("Joshua Bloch", new ArrayList<>()),
                "Programming",
                "Addison-Wesley",
                "http://example.com/effective_java",
                "EPUB");

        assertEquals(expectedEBook, author.getItems().get(0));
    }
    @Test
    void testThatWrongItemTypeThrowsException() {
        String[] bookValues = {"Educated", "Tara Westover", "Biography", "Random House", "9780399590504", "Pocket" };
        assertThrows(IllegalArgumentException.class, () -> itemFactory.createItem(bookValues, "Magazines"));
    }
    @Test
    void testThatInvalidValueCountThrowsException() {
        String[] incompleteBookValues = {"Educated", "Tara Westover", "Biography", "Random House" };
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> itemFactory.createItem(incompleteBookValues, "Books"));
    }
    @Test
    void testThatInvalidUrlThrowsException() {
        String[] ebookValues = {"Effective Java", "Joshua Bloch", "Programming", "Addison-Wesley", "example.com/effective_java", "EPUB" };
        assertThrows(IllegalArgumentException.class, () -> itemFactory.createItem(ebookValues, "Ebooks"));
    }
    @Test
    void testThatInvalidNumberOfValuesForEbookThrowsException() {
        String[] ebookValues = {"Effective Java", "Joshua Bloch", "Programming", "Addison-Wesley", "http://example.com/effective_java"};
        assertThrows(IllegalArgumentException.class, () -> itemFactory.createItem(ebookValues, "Ebooks"));
    }
    @Test
    void testThatInvalidNumberOfValuesForReferenceThrowsException() {
        String[] referenceValues = {"Effective Java", "Joshua Bloch", "Programming", "Addison-Wesley"};
        assertThrows(IllegalArgumentException.class, () -> itemFactory.createItem(referenceValues, "References"));
    }
    @Test
    void testThatCreateItemsFromCsvReturnsListWithItems() throws IOException {
        when(mockBufferedReader.readLine()).thenReturn("Educated,Tara Westover,Biography,Random House,9780399590504,Pocket").thenReturn(null);

        List<Item> items = itemFactory.createItemsFromCsv(mockBufferedReader, "Books");

        Book expected = new Book(
                "Educated",
                new Author("Tara Westover", new ArrayList<>()),
                "Biography",
                "Random House",
                "9780399590504",
                CoverType.POCKET);

        assertEquals(expected, items.get(0));
    }


}
