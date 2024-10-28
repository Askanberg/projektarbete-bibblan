package org.bibblan.feemanagement;

import static org.junit.jupiter.api.Assertions.*;

import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.loanmanagement.Loan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.bibblan.GeneralTestData;

import java.time.LocalDate;

public class FeeTest {

    private Fee feeManagement;
    private Loan mockLoan;

    @BeforeEach
    void setUp() {
        feeManagement = new Fee();
        mockLoan = new Loan(new Book("Test Book", "Author", "Genre"));

        mockLoan.setDueDate(LocalDate.now().minusDays(5));
    }

    @Test
    void testCalculateFine() {
        double fine = feeManagement.calculateFine(mockLoan);
        assertTrue(fine > 0, "Fine should be greater than zero for overdue loans.");
    }

    void testAddFee() {
        feeManagement.addFee(50.0);
        assertEquals(50.0, feeManagement.getTotalFees(), "Total fee should bo 50.0 after adding.");

        feeManagement.addFee(25.0);
        assertEquals(75.0, feeManagement.getTotalFees(), "Total fee should be 75.0 after adding another fee.")
    }

    void testPayFee() {
        feeManagement.addFee(50.0);
        feeManagement.addFee(100.0);

        assertEquals(150.0, feeManagement.getTotalFees(), "Total fee should be 150.0");
    }
}
