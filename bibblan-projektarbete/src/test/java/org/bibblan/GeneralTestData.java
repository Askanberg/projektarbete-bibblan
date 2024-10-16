package org.bibblan;

import org.bibblan.bookcatalog.Author;
import org.bibblan.bookcatalog.Book;
import org.bibblan.bookcatalog.CoverType;
import org.bibblan.bookcatalog.Item;
import org.bibblan.usermanagement.service.UserService;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public User createLocalTestUserA(){
        return User.builder() // Dummy-objekt utan ID eftersom det inte initialiseras utanför databas-integrationen.
                .email("anna@gmail.com")
                .userName("Anna_Book")
                .name("Anna Book")
                .password("SzrqTlst_#")
                .build();
    }

}
