package org.bibblan.loanmanagement;

import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.BeforeEach;

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
}