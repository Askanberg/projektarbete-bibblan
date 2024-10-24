package org.bibblan.loanmanagement;

import org.bibblan.bookcatalog.Book;
import org.bibblan.bookcatalog.Author;
import org.bibblan.bookcatalog.CoverType;
import org.bibblan.loanmanagement.Loan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LoanTest {
    private Book book;
    private Loan loan;
    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author("F. Scott Fitzgerald");
        book = new Book("The Great Gatsby", author, "Classic", "9780743273565", "Scribner", CoverType.HARDCOVER);
        loan = new Loan(book);
    }

    @Test
    void testLoanCreation() {
        assertNotNull(loan);
        assertEquals(LocalDate.now(), loan.getStartDate(), "Start date should be today's date");
        assertEquals(LocalDate.now().plusDays(loan.getLoanDuration()), loan.getDueDate(), "Due date should be correct");
        assertFalse(loan.isReturned(), "The book should not be returned upon creation");
    }
}