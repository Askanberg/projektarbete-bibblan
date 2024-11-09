package org.bibblan.loanmanagement;

import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
    @Test
    public void testReturnLoanSuccessfully() {
        loanCollections.addLoan(user, book);
        Loan loan = loanCollections.getUserLoans(user).get(0);

        loanCollections.returnLoan(user, loan);

        assertFalse(loanCollections.isBookLoaned(book));
        assertEquals("Returned", loan.getLoanStatus());
    }

    @Test
    public void testReturnLoanThrowsExceptionWhenNoSuchLoanExists() {
        assertThrows(NoSuchElementException.class, () -> loanCollections.returnLoan(user, loan), "No loan found for this book by the user.");
    }

    @Test
    public void testMarkLoanAsLostSuccessfully() {
        loanCollections.addLoan(user, book);
        Loan loan = loanCollections.getUserLoans(user).get(0);

        loanCollections.markLoanAsLost(user, loan);

        assertEquals("Lost", loan.getLoanStatus());
        assertTrue(loan.isLost());
    }

    @Test
    public void testMarkLoanAsLostThrowsExceptionWhenNoSuchLoanExists() {
        assertThrows(NoSuchElementException.class, () -> loanCollections.markLoanAsLost(user, loan), "No active loan found for this book by the user.");
    }

    @Test
    public void testAutoRenewLoanSuccessfully() {
        loanCollections.addLoan(user, book);
        Loan loan = loanCollections.getUserLoans(user).get(0);

        int originalDaysRemaining = loan.getRemainingDays();
        loanCollections.autoRenewLoan(user, loan);

        assertEquals(originalDaysRemaining + (loan.getLoanDuration() / 2), loan.getRemainingDays());
        assertEquals(1, loan.getRenewals());
    }

    @Test
    public void testAutoRenewLoanThrowsExceptionWhenMaxRenewalsReached() {
        Book book = mock(Book.class);
        when(book.getGenre()).thenReturn("Fiction");

        Loan loan = new Loan(book);
        loan.setLoanDuration();

        User user = mock(User.class);

        LoanCollections loanCollections = new LoanCollections();
        loanCollections.addLoan(user, book);

        Loan addedLoan = loanCollections.getAllActiveLoans().get(user).get(0);

        for (int i = 0; i < Loan.MAX_RENEWALS; i++) {
            loanCollections.autoRenewLoan(user, addedLoan);
        }

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> loanCollections.autoRenewLoan(user, addedLoan));

        assertEquals("Cannot auto-renew loan. Maximum renewals reached or loan is overdue/returned.", exception.getMessage());
    }

    @Test
    public void testAutoRenewLoanThrowsExceptionWhenNoSuchLoanExists() {
        assertThrows(NoSuchElementException.class, () -> loanCollections.autoRenewLoan(user, loan), "No active loan found for this book by the user.");
    }

    @Test
    public void testGetUserLoansReturnsCorrectLoans() {
        loanCollections.addLoan(user, book);
        Book book2 = mock(Book.class);
        loanCollections.addLoan(user, book2);

        List<Loan> userLoans = loanCollections.getUserLoans(user);
        assertEquals(2, userLoans.size());
    }

    @Test
    public void testGetAllActiveLoansReturnsAllLoans() {
        User user2 = mock(User.class);
        Book book2 = mock(Book.class);

        loanCollections.addLoan(user, book);
        loanCollections.addLoan(user2, book2);

        assertEquals(2, loanCollections.getAllActiveLoans().size());
    }
}