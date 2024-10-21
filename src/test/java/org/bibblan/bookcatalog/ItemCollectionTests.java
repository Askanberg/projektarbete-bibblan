package org.bibblan.bookcatalog;

import org.bibblan.bookcatalog.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class ItemCollectionTests {
    ItemCollection itemCollection;

    @BeforeEach
    public void initializeUtilities() {
        itemCollection = new ItemCollection();
    }
    @Test
    void testAddSingleItem() {
        Book book = new Book("Educated", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "9780399590504", CoverType.POCKET);

        itemCollection.addItem(book);

        assertEquals(book, itemCollection.getItemCount("Educated").getTotalCopies().get(0));
    }
    @Test
    void testAddMultipleItems() throws IOException {
        ItemFileReader itemFileReader = new ItemFileReader(new ItemFactory());
        List<Item> items = itemFileReader.readItemsFromCsv("src/test/resources/testBooks.csv");

        itemCollection.addItems(items);

        for (Item i : items) {
            assertTrue(itemCollection.getItemMap().containsKey(i.getTitle()));
        }
    }

    @Test
    void testThatAddNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> itemCollection.addItem(null));
    }

    @Test
    void testThatAddingTwoOfTheSameBooksIncreasesCount() {
        Book bookA = new Book("Educated", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "9780399590504", CoverType.POCKET);

        itemCollection.addItem(bookA);
        int expectedAmountOfCopies = itemCollection.getItemMap().get(bookA.getTitle()).getCount();

        itemCollection.addItem(bookA);

        assertEquals(expectedAmountOfCopies + 1, itemCollection.getItemCount(bookA.getTitle()).getCount());
    }

    @Test
    void testThatAddingTwoOfTheSameBooksButDifferentTypesIncreasesCount() {
        Book book = new Book("Educated", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "9780399590504", CoverType.POCKET);
        EBook eBook = new EBook("Educated", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "http://example.com/educated", "EPUB");

        itemCollection.addItem(book);
        int expectedAmountOfCopies = itemCollection.getItemMap().get(book.getTitle()).getCount();

        itemCollection.addItem(eBook);

        assertEquals(expectedAmountOfCopies + 1, itemCollection.getItemCount(book.getTitle()).getCount());
    }
    @Test
    void testThatGetCopiesReturnsBothBooksAfterAddingTwoOfTheSameBooksButDifferentTypes() {
        Book book = new Book("Educated", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "9780399590504", CoverType.POCKET);
        EBook eBook = new EBook("Educated", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "http://example.com/educated", "EPUB");

        List<Item> expectedItems = new ArrayList<>();
        expectedItems.add(book);
        expectedItems.add(eBook);

        itemCollection.addItems(expectedItems);

        assertEquals(expectedItems, itemCollection.getItemCount("Educated").getTotalCopies());
    }

    @Test
    void testAddingDifferentItemTypes() throws IOException {
        ItemFileReader itemFileReader = new ItemFileReader(new ItemFactory());
        List<Item> ebooks =  itemFileReader.readItemsFromCsv("src/test/resources/testEBooks.csv");
        List<Item> books =  itemFileReader.readItemsFromCsv("src/test/resources/testBooks.csv");

        EBook ebook = (EBook) ebooks.get(0);
        Book book = (Book) books.get(0);

        itemCollection.addItems(ebooks);
        itemCollection.addItems(books);


        assertTrue(itemCollection.getItemMap().containsKey(ebook.getTitle()), "Ebook");
        assertTrue(itemCollection.getItemMap().containsKey(book.getTitle()), "Book");

    }
}
