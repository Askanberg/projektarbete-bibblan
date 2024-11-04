package org.bibblan.loanmanagement;

import java.util.*;

import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.usermanagement.user.User;

public class LoanCollections {
    private Map<User, List<Loan>> activeLoans;

    public LoanCollections() {
        this.activeLoans = new LinkedHashMap<>();
    }

    public boolean isBookLoaned(Book book) {
        for (List<Loan> l : activeLoans.values()) {
            if (l.contains(book)) {
                return true;
            }
        }
        return false;
    }

    public void addLoan(User user, Book book) {
        if (isBookLoaned(book)) {
            throw new IllegalStateException("This book is already loaned out.");
        }
        Loan newLoan = new Loan(book);
        if (activeLoans.containsKey(user)) {
            activeLoans.get(user).add(newLoan);
        } else {
            List<Loan> loans = new ArrayList<>();
            loans.add(newLoan);
            activeLoans.put(user, loans);
        }
    }

    public void returnLoan(User user, Loan loan) {
        if (activeLoans.containsKey(user)) {
            if (activeLoans.get(user).contains(loan)) {
                loan.returnBook();
                activeLoans.get(user).remove(loan);
            } else {
                throw new NoSuchElementException("User has no loan of that book.");
            }
        } else{
            throw new NoSuchElementException("No such user has a loan.");
        }
    }

    public Map<User, List<Loan>> getAllActiveLoans() {
        return activeLoans;
    }

    public void clearLoans() {
        activeLoans.clear();
    }
}
