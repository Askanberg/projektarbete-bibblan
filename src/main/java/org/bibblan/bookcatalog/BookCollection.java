package org.bibblan.bookcatalog;

import org.bibblan.bookcatalog.domain.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookCollection {
    Map<String, Book> bookMap = new HashMap<>();
    Map<String, EBook> digitalBookMap = new HashMap<>();
    Map<String, Reference> referenceMap = new HashMap<>();


    public Map<String, Book> readBooksFromCsv(String filePath) throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line == null) {
                throw new IllegalArgumentException("Empty file");
            }
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Book book = makeBook(values);
                bookMap.put(book.getIsbn(), book);
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookMap;

    }

    private Book makeBook(String[] values) {
        switch (values.length) {
            case 6:
                String title = values[0].trim();
                Author author = new Author(values[1].trim(), new ArrayList<>());
                String genre = values[2].trim();
                String isbn = values[3].trim();
                String publisher = values[4].trim();
                String coverType = values[5].trim();
                return new Book(title, author, genre, isbn, publisher, CoverType.valueOf(coverType.toUpperCase()));
            default:
                throw new IllegalArgumentException("Invalid number of columns");
        }
    }
}

