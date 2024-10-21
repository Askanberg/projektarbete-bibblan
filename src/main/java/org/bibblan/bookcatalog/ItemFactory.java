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
            try {
                Item item = createItem(values, itemType);
                items.add(item);
            } catch (IllegalArgumentException e) {
                System.err.println("Skipping invalid item: " + e.getMessage());
            }
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
                if (!isbnBook.matches("\\d+")) {
                    throw new IllegalArgumentException("Invalid ISBN for book: " + isbnBook);
                }
                if (values.length != 6) {
                    throw new IllegalArgumentException("Invalid number of columns for book");
                }
                String coverType = values[5].trim();
                return new Book(title, author, genre, publisher, isbnBook, CoverType.valueOf(coverType.toUpperCase()));
            case "ebooks":
                String url = values[4].trim();
                if (!url.startsWith("http") && !url.startsWith("www")) {
                    throw new IllegalArgumentException("Invalid URL for Ebook: " + url);
                }
                if (values.length != 6) {
                    throw new IllegalArgumentException("Invalid number of columns for book");
                }
                String fileFormat = values[5].trim();
                return new EBook(title, author, genre, publisher, url, fileFormat);
            case "references":
                if (values.length != 5) {
                    throw new IllegalArgumentException("Invalid number of columns for book");
                }
                String isbnReference = values[4].trim();
                return new Reference(title, author, genre, publisher, isbnReference);
            default:
                throw new IllegalArgumentException("Invalid itemType: " + itemType);
        }
    }

}
