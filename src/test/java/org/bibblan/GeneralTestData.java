package org.bibblan;

import org.bibblan.bookcatalog.domain.Author;
import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.bookcatalog.domain.CoverType;

import org.bibblan.usermanagement.user.User;

import java.util.ArrayList;

public class GeneralTestData {
    //Books and domain classes
    public Book createTestBookA() {
        Author author = new Author("NameA", new ArrayList<>());
        return new Book("TitleA", author, "Genre", "100-00-00-0-000", "Publisher", CoverType.HARDCOVER);
    }
    public Book createTestBookB() {
        Author author = new Author("NameB", new ArrayList<>());
        return new Book("TitleB", author, "Genre", "200-00-00-0-000", "Publisher", CoverType.HARDCOVER);
    }
    public Book createTestBookC() {
        Author author = new Author("NameC", new ArrayList<>());
        return new Book("TitleC", author, "Genre", "300-00-00-0-000", "Publisher", CoverType.HARDCOVER);
    }
    //Users
    public User createLocalTestUserA(){
        return User.builder() // Dummy-objekt utan ID eftersom det inte initialiseras utanför databas-integrationen.
                .email("anna@gmail.com")
                .username("Anna_Book")
                .name("Anna Book")
                .password("SzrqTlst_#")
                .build();
    }
}
