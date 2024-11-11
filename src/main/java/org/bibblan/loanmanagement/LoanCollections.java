package org.bibblan.loanmanagement;

import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.usermanagement.user.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoanCollections {

    private final Map<User, List<Loan>> activeLoans = new LinkedHashMap<>();


    public boolean isBookLoaned(Book book) {
        for (List<Loan> loans : activeLoans.values()) {
            for (Loan loan : loans) {
                if (loan.getItem().equals(book) && loan.getLoanStatus().equals("Active")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addLoan(User user, Book book) {
        if (isBookLoaned(book)) {
            throw new IllegalStateException("This book is already loaned out.");
        }

        List<Loan> userLoans = activeLoans.getOrDefault(user, new ArrayList<>());
        if (userLoans.size() >= Loan.MAX_LOANS) {
            throw new IllegalStateException("User has reached the maximum loan limit.");
        }

        Loan newLoan = new Loan(book);
        userLoans.add(newLoan);
        activeLoans.put(user, userLoans);
    }

    public void returnLoan(User user, Loan loan) {
        List<Loan> userLoans = activeLoans.get(user);
        if (userLoans != null && userLoans.contains(loan)) {
            loan.returnBook();
            userLoans.remove(loan);
        } else {
            throw new NoSuchElementException("No loan found for this book by the user.");
        }
    }

    public void markLoanAsLost(User user, Loan loan) {
        List<Loan> userLoans = activeLoans.get(user);
        if (userLoans != null && userLoans.contains(loan) && !loan.isReturned()) {
            loan.markAsLost();
        } else {
            throw new NoSuchElementException("No active loan found for this book by the user.");
        }
    }

    public void autoRenewLoan(User user, Loan loan) {
        List<Loan> userLoans = activeLoans.get(user);
        if (userLoans != null && userLoans.contains(loan)) {
            if (loan.getRenewals() < Loan.MAX_RENEWALS && !loan.isReturned() && !loan.isOverdue()) {
                loan.autoRenewLoan();
            } else {
                throw new IllegalStateException("Cannot auto-renew loan. Maximum renewals reached or loan is overdue/returned.");
            }
        } else {
            throw new NoSuchElementException("No active loan found for this book by the user.");
        }
    }

    public List<Loan> getUserLoans(User user) {
        return activeLoans.getOrDefault(user, new ArrayList<>());
    }

    public Map<User, List<Loan>> getAllActiveLoans() {
        return activeLoans;
    }
}
