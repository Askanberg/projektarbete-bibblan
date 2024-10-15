package org.bibblan;

import org.bibblan.bookcatalog.domain.Author;
import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.bookcatalog.domain.CoverType;

public class GeneralTestData {
    //Books and domain classes
    public Book createTestBookA() {
        Author author = new Author();
        return new Book("TitleA", author, "Genre", "100-00-00-0-000", "Publisher", CoverType.HARDCOVER);
    }
    public Book createTestBookB() {
        Author author = new Author();
        return new Book("TitleB", author, "Genre", "200-00-00-0-000", "Publisher", CoverType.HARDCOVER);
    }
    public Book createTestBookC() {
        Author author = new Author();
        return new Book("TitleC", author, "Genre", "300-00-00-0-000", "Publisher", CoverType.HARDCOVER);
    }
    //Users




}
