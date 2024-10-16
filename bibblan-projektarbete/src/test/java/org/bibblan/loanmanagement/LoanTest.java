package org.bibblan.loanmanagement;

import org.bibblan.bookcatalog.Book;
import org.bibblan.GeneralTestData;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;


class LoanTest {
    private Book book;
    private Loan loan;

    @BeforeEach
    void setUp() {
        Author author = new Author("F. Scott Fitzgerald");
        book = new Book("The Great Gatsby", author, "Classic", "9780743273565", "Scribner", CoverType.HARDCOVER);
        loan = new Loan(book);
    }

    @Test
    void testLoanCreation() { // Testar att en loan instans skapas korrekt med rätt slut och start datum
        assertNotNull(loan);
        assertEquals(LocalDate.now(), loan.getStartDate(), "Start date should be today's date");
        assertEquals(LocalDate.now().plusDays(book.getLoanDuration()), loan.getDueDate(), "Due date should be correct");
        assertFalse(loan.isReturned(), "The book should not be returned upon creation");
    }



}