package org.bibblan.bookcatalog;


import lombok.Data;
import org.bibblan.bookcatalog.domain.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
public class ItemCollection {
    Map<String, Item> itemMap = new HashMap<>();


    public void readItemsFromCsv(String filePath) throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String itemType = br.readLine();
            if (itemType == null) {
                throw new IllegalArgumentException("Empty file");
            }
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Item item = makeItems(values, itemType);
                if (item instanceof Book book) {
                    itemMap.put(book.getIsbn(), book);
                } else if (item instanceof EBook eBook) {
                    itemMap.put(eBook.getUrl(), eBook);
                }
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Item makeItems(String[] values, String itemType) {
        String title = values[0].trim();
        Author author = new Author(values[1].trim(), new ArrayList<>());
        String genre = values[2].trim();
        String publisher = values[3].trim();
        switch (itemType) {
            case "books":
                String isbn = values[4].trim();
                String coverType = values[5].trim();
                return new Book(title, author, genre, publisher, isbn, CoverType.valueOf(coverType.toUpperCase()));
            case "ebooks":
                String url = values[4].trim();
                String fileFormat = values[5].trim();
                return new EBook(title, author, genre, publisher, url, fileFormat);
            default:
                throw new IllegalArgumentException("Invalid itemType in Csv-file: " + itemType);
        }
    }
}
