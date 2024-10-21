package org.bibblan.bookcatalog;

import org.bibblan.bookcatalog.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ItemCollectionTests {
    ItemCollection itemCollection;

    @BeforeEach
    public void initializeUtilities() {
        itemCollection = new ItemCollection();
    }

    @Test
    void testThatAddItemsAddsCorrectlyToMap() throws IOException {
        ItemFileReader itemFileReader = new ItemFileReader(new ItemFactory());

        ArrayList<Item> items = (ArrayList<Item>) itemFileReader.readItemsFromCsv("src/test/resources/testBooks.csv");

        itemCollection.addItems(items);

        for (Item i : items) {
            assertTrue(itemCollection.getItemMap().containsValue(i));
        }
    }
}
