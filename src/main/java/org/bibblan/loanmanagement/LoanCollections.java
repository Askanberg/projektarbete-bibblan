package org.bibblan.loanmanagement;

import java.util.ArrayList;
import java.util.List;
import org.bibblan.bookcatalog.domain.Book;

public class LoanCollections {
    private List<Loan> activeLoans;

    public LoanCollections() {
        this.activeLoans = new ArrayList<>();
    }

    public boolean isBookLoaned(Book book) {
        for (Loan loan : activeLoans) {
            if (loan.getItem().equals(book) && !loan.isReturned()) {
                return true;
            }
        }
        return false;
    }

    public Loan addLoan(Book book) {
        if (isBookLoaned(book)) {
            throw new IllegalStateException("This book is already loaned out.");
        }
        Loan newLoan = new Loan(book);
        activeLoans.add(newLoan);
        return newLoan;
    }

    public void returnLoan(Loan loan) {
        if (activeLoans.contains(loan)) {
            loan.returnBook();
        }
    }

    public List<Loan> getAllActiveLoans() {
        return new ArrayList<>(activeLoans);
    }

    public void clearLoans() {
        activeLoans.clear();
    }
}
