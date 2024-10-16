package org.bibblan.bookcatalog;

import org.bibblan.bookcatalog.domain.Author;
import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.bookcatalog.domain.CoverType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookCollection {
    Map<String, Book> bookMap = new HashMap<>();

    public Map<String, Book> readBooksFromCsv(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Book book = makeBook(values);
                bookMap.put(book.getIsbn(), book);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookMap;

    }

    private Book makeBook(String[] values) {
        String title = values[0].trim();
        Author author = new Author(values[1].trim(), new ArrayList<>());
        String genre = values[2].trim();
        String isbn = values[3].trim();
        String publisher = values[4].trim();
        String coverType = values[5].trim();

        return new Book(title, author, genre, isbn, publisher, CoverType.valueOf(coverType.toUpperCase()));
    }
}
