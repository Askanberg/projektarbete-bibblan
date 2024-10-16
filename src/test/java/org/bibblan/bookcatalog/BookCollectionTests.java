package org.bibblan.bookcatalog;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class BookCollectionTests {

    @Test
    void testThatReadItemsFromCsvCanReadBookCsv() throws FileNotFoundException {
        BookCollection tempCollection = new BookCollection();
        tempCollection.readItemsFromCsv("src/test/resources/testBooks.csv");
        assertTrue(tempCollection.getBookMap().containsKey("9781400052172"));
    }

    @Test
    void testThatReadItemsFromCsvThrowsExceptionIfNoSuchFile() {
        BookCollection tempCollection = new BookCollection();
        assertThrows(FileNotFoundException.class, () -> {
            tempCollection.readItemsFromCsv("src/test/resources/NoSuchFile.csv");
        });
    }

    @Test
    void testThatReadItemsFromCsvThrowsExceptionIfFileIsEmpty() {
        BookCollection tempCollection = new BookCollection();
        assertThrows(IllegalArgumentException.class, () -> {
            tempCollection.readItemsFromCsv("src/test/resources/emptyBookFile.csv");
        });
    }

    @Test
    void testThatMakeItemThrowsExceptionIfNumberOfColumnsIsInvalid() {
        BookCollection tempCollection = new BookCollection();
        assertThrows(IllegalArgumentException.class, () -> {
            tempCollection.readItemsFromCsv("src/test/resources/testEBooks.csv");
        });
    }


    @Test
    void testThatReadItemsFromCsvCanReadDigitalBookCsv() throws FileNotFoundException {
        BookCollection tempCollection = new BookCollection();
        tempCollection.readItemsFromCsv("src/test/resources/testEBooks.csv");
        assertTrue(tempCollection.getEBookMap().containsKey("9781400052172"));

    }



}
