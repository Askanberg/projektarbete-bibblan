package org.bibblan.bookcatalog;

import lombok.Data;
import org.bibblan.bookcatalog.domain.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ItemFactory {
    private Map<String, Author> authorMap = new HashMap<>();

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
        Author author = getOrCreateAuthor(values[1].trim());

        String genre = values[2].trim();
        String publisher = values[3].trim();
        switch (itemType.toLowerCase()) {
            case "books":
                return createBook(values, title, author, genre, publisher);
            case "ebooks":
                return createEbook(values, title, author, genre, publisher);
            case "references":
                return createReference(values, title, author, genre, publisher);
            default:
                throw new IllegalArgumentException("Invalid itemType: " + itemType);
        }
    }

    private Author getOrCreateAuthor(String name) {
        if (!authorMap.containsKey(name)) {
            return new Author(name, new ArrayList<>());

        } else {
            return authorMap.get(name);
        }
    }

    private Book createBook(String[] values, String title, Author author, String genre, String publisher) {
        String isbnBook = values[4].trim();
        if (!isbnBook.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid ISBN for book: " + isbnBook);
        }
        if (values.length != 6) {
            throw new IllegalArgumentException("Invalid number of columns for book");
        }
        String coverType = values[5].trim();
        Book book = new Book(title, author, genre, publisher, isbnBook, CoverType.valueOf(coverType.toUpperCase()));
        author.addItem(book);
        authorMap.put(author.getName(), author);
        return book;
    }
    private EBook createEbook(String[] values, String title, Author author, String genre, String publisher) {
        String url = values[4].trim();
        if (!url.startsWith("http") && !url.startsWith("www")) {
            throw new IllegalArgumentException("Invalid URL for Ebook: " + url);
        }
        if (values.length != 6) {
            throw new IllegalArgumentException("Invalid number of columns for book");
        }
        String fileFormat = values[5].trim();
        EBook ebook = new EBook(title, author, genre, publisher, url, fileFormat);
        author.addItem(ebook);
        authorMap.put(author.getName(), author);
        return ebook;
    }
    private Reference createReference(String[] values, String title, Author author, String genre, String publisher) {
        if (values.length != 5) {
            throw new IllegalArgumentException("Invalid number of columns for book");
        }
        String isbnReference = values[4].trim();
        return new Reference(title, author, genre, publisher, isbnReference);
    }

}
