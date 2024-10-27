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
    void testReturnBook() {
        loan.returnBook();
        assertTrue(loan.isReturned(), "Loan should be returned");
        assertEquals(0.0, loan.calculateFine(), "Fine should be 0 if loan is not overdue.");
    }

    @Test
    void testExtendLoan() {
        loan.extendLoan(7);
        assertEquals(21, loan.getRemainingDays(), "Remaining days should be updated");
    }

    @Test
    void testGetLoanStatus_Active() {
        assertEquals("Active", loan.getLoanStatus(), "Loan status should be 'Active' upon creation." );
    }

    @Test
    void testGetLoanStatus_Returned() {
        loan.returnBook();
        assertEquals("Returned", loan.getLoanStatus(), "Loan status should be 'Returned' upon creation");
    }

    @Test
    void testGetRemainingDays_NotReturned() {
        loan.setDueDate(LocalDate.now().plusDays(10));
        assertEquals(10, loan.getRemainingDays(), "Remaining days should reflect the correct amount of time.");
    }

    @Test
    void testGetRemainingDays_Returned() {
        loan.returnBook();
        assertEquals(0, loan.getRemainingDays(), "Remaining days should be 0 if the loan is returned.");
    }

    @Test
    void testMarkAsLost() {
        loan.markAsLost();
        assertTrue(loan.isLost(), "Loan should be marked as lost.");
        assertEquals("Lost", loan.getLoanStatus(), "Loan status should be 'Lost' when marked as lost.");

        List<String> history = loan.getLoanHistory();
        assertTrue(history.contains("Book marked as lost on " + LocalDate.now()), "Loan history should include the lost status.");
    }

}
