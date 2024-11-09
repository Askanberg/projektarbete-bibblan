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
