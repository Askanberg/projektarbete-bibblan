package org.bibblan.bookcatalog;

import org.bibblan.bookcatalog.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemFileReaderTests {
    private ItemFileReader itemFileReader;

    @BeforeEach
    public void initializeUtilities() {
        ItemFactory itemFactory = new ItemFactory();
        itemFileReader = new ItemFileReader(itemFactory);
    }

    @Test
    void testReadValidBookCsvFile() throws IOException {
        List<Item> items = itemFileReader.readItemsFromCsv("src/test/resources/testBooks.csv");

        Book expected = new Book(
                "Educated",
                new Author("Tara Westover", new ArrayList<>()),
                "Biography",
                "Random House",
                "9780399590504",
                CoverType.POCKET);

        assertTrue(items.contains(expected));
    }

    @Test
    void testThatMixedItemsInFilesSkipsWrongItemsWhenCreatingList() throws IOException {
        List<Item> items = itemFileReader.readItemsFromCsv("src/test/resources/mixedItemTypes.csv");
        Book expectedBook = new Book(
                "Educated",
                new Author("Tara Westover", new ArrayList<>()),
                "Biography",
                "Random House",
                "9780399590504",
                CoverType.POCKET);
        List<Item> expectedList = new ArrayList<>();
        expectedList.add(expectedBook);
        assertEquals(expectedList, items);

    }

    @Test
    void testThatReadBooksFromCsvThrowsExceptionIfFileIsEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
                itemFileReader.readItemsFromCsv("src/test/resources/emptyFile.csv"));
    }

    @Test
    void testThatReadItemsFromCsvThrowsExceptionIfFileNotFound() {
        assertThrows(FileNotFoundException.class, () -> {
            itemFileReader.readItemsFromCsv("src/test/resources/NoSuchFile.csv");
        });
    }

}
