package org.bibblan;

import org.bibblan.bookcatalog.ItemCollection;
import org.bibblan.bookcatalog.ItemFactory;
import org.bibblan.bookcatalog.ItemFileReader;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTests {
    private ItemCollection itemCollection;
    private ItemFileReader itemFileReader;


    @BeforeEach
    void setup() throws IOException {
        itemCollection = new ItemCollection();
        itemFileReader = new ItemFileReader(new ItemFactory());
        itemFileReader.readItemsFromCsv("src/test/resources/testBooks.csv");


    }




}
