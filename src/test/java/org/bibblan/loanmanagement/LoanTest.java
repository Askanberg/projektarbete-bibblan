package org.bibblan.loanmanagement;


import org.bibblan.bookcatalog.domain.Author;
import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.bookcatalog.domain.CoverType;

import org.bibblan.bookcatalog.domain.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;


class LoanTest {
    private Book book;
    private Loan loan;
    private Author author;
    private List<Item> items;


    @BeforeEach
    void setUp() {
        author = new Author("F. Scott Fitzgerald", items);
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

    @Test
    void testCalculateFine_NoFineIFNotOverdue(){
        assertEquals(0.0, loan.calculateFine(), "Fine should be 0 if loan is not overdue.");
    }

    @Test
    void testCalculateFine_WithOverdue() {
        loan.setDueDate(LocalDate.now().minusDays(5));
        assertEquals(50.0, loan.calculateFine(), "Fine should be 50 if loan is overdue.");
    }

    @Test
    void testIsOverdue_WhenLoanIsOverdue() {
        loan.setDueDate(LocalDate.now().minusDays(7));
        assertTrue(loan.isOverdue(), "Loan should be overdue");
    }

    @Test
    void testIsOverdue_WhenLoanIsNotOverdue() {
        assertFalse(loan.isOverdue(), "Loan should not be overdue");
    }

    @Test
    void testReturnLoan(){
        loan.returnLoan();
        assertTrue(loan.isReturned(), "Loan should be returned");
        assertEquals(0.0, loan.calculateFine(), "Fine should be 0 if loan is not overdue.");
    }
}
