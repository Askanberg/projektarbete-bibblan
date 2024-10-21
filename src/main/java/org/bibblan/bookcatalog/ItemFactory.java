package org.bibblan.bookcatalog;

import lombok.Data;
import org.bibblan.bookcatalog.domain.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemFactory {

    public List<Item> createItemsFromCsv(BufferedReader br, String itemType) throws IOException {
        List<Item> items = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            Item item = createItem(values, itemType);
            items.add(item);
        }
        return items;
    }

    public Item createItem(String[] values, String itemType) {
        String title = values[0].trim();
        Author author = new Author(values[1].trim(), new ArrayList<>());
        String genre = values[2].trim();
        String publisher = values[3].trim();
        switch (itemType.toLowerCase()) {
            case "books":
                String isbnBook = values[4].trim();
                String coverType = values[5].trim();
                return new Book(title, author, genre, publisher, isbnBook, CoverType.valueOf(coverType.toUpperCase()));
            case "ebooks":
                String url = values[4].trim();
                String fileFormat = values[5].trim();
                return new EBook(title, author, genre, publisher, url, fileFormat);
            case "references":
                String isbnReference = values[4].trim();
                return new Reference(title, author, genre, publisher, isbnReference);
            default:
                throw new IllegalArgumentException("Invalid itemType: " + itemType);
        }
    }

}
