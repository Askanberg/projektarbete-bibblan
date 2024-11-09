package org.bibblan.loanmanagement;

import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanCollectionsTest {
    private LoanCollections loanCollections;
    private User user;
    private Book book;
    private Loan loan;

    @BeforeEach
    public void setUp() {
        loanCollections = new LoanCollections();
        user = mock(User.class);
        book = mock(Book.class);
        loan = mock(Loan.class);

        // Mock Loan behavior
        when(loan.getItem()).thenReturn(book);
        when(loan.getLoanStatus()).thenReturn("Active");
    }

    @Test
    public void testIsBookLoanedWhenBookIsNotLoaned() {
        assertFalse(loanCollections.isBookLoaned(book));
    }

    @Test
    public void testIsBookLoanedWhenBookIsLoaned() {
        loanCollections.addLoan(user, book);
        assertTrue(loanCollections.isBookLoaned(book));
    }

    @Test
    public void testAddLoanSuccessfully() {
        loanCollections.addLoan(user, book);

        assertTrue(loanCollections.getAllActiveLoans().containsKey(user));
        assertEquals(1, loanCollections.getAllActiveLoans().get(user).size());

        assertTrue(loanCollections.isBookLoaned(book));
    }
    @Test
    public void testAddLoanThrowsExceptionWhenBookIsAlreadyLoaned() {
        loanCollections.addLoan(user, book);
        assertThrows(IllegalStateException.class, () -> loanCollections.addLoan(user, book), "This book is already loaned out.");
    }

    @Test
    public void testAddLoanThrowsExceptionWhenUserMaxLoansExceeded() {
        loanCollections.addLoan(user, book);
        Book book2 = mock(Book.class);
        Book book3 = mock(Book.class);
        Book book4 = mock(Book.class);

        loanCollections.addLoan(user, book2);
        loanCollections.addLoan(user, book3);

        assertThrows(IllegalStateException.class, () -> loanCollections.addLoan(user, book4), "User has reached the maximum loan limit.");
    }
}