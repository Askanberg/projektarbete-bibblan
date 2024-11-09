package org.bibblan.loanmanagement;

import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
}