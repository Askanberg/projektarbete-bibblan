package org.bibblan.bookcatalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bibblan.bookcatalog.domain.Item;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemFileReader {
    private final ItemFactory itemFactory;


    public List<Item> readItemsFromCsv(String filepath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String itemType = br.readLine();
            if (itemType == null) {
                throw new IllegalArgumentException("File is empty");
            }

            br.readLine(); //Skips the column titles
            return itemFactory.createItemsFromCsv(br, itemType);
        }
    }
}
