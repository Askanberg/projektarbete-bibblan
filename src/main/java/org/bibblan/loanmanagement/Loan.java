package org.bibblan.loanmanagement;


import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


import lombok.Data;
import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.bookcatalog.domain.Item;


@Data
public class Loan {

    private static final double DAILY_FINE_RATE = 10.0;
    public static final int MAX_RENEWALS = 3;
    public static final int MAX_LOANS = 3;
    private Book item;
    public LocalDate startDate;
    private LocalDate dueDate;
    private int loanDuration;
    private boolean returned = false;
    private List<String> loanHistory = new ArrayList<>();
    private boolean lost = false;
    private String status;
    private int renewals = 0;
    private static int totalLoans = 0;


    public Loan(Book book) {
        if (totalLoans >= MAX_LOANS) {
            throw new IllegalStateException("Cannot borrow more than " + MAX_LOANS + " copies of this book.");
        }
        this.item = book;
        this.startDate = LocalDate.now();
        setLoanDuration();
        this.dueDate = startDate.plusDays(this.loanDuration);
    }


    public void setLoanDuration() {
        if ("Classics".equalsIgnoreCase(item.getGenre())) {
            this.loanDuration = 28;
        } else {
            this.loanDuration = 14;
        }
    }


    public void returnBook() {
        loanHistory.add("Book returned on " + LocalDate.now());
        this.returned = true;
    }

    public boolean isReturned() {
        return returned;
    }


    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate) && !returned;
    }


    public double calculateFine() {
        if (!returned && isOverdue()) {
            long overdueDays = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
            return overdueDays * Loan.DAILY_FINE_RATE;
        }
        return 0.0;
    }


    public void extendLoan(int extraDays) {
        if (!isOverdue() && !returned) {
            dueDate = dueDate.plusDays(extraDays);
        }
    }

    public int getRemainingDays() {
        if (returned) return 0;
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    public String getLoanStatus() {
        if (returned) {
            return "Returned";
        } else if (lost) {
            return "Lost";
        } else if (isOverdue()){
            return "Overdue";
        } else {
            return "Active";
        }
    }

    public List<String> getLoanHistory() {
        return new ArrayList<>(loanHistory);
    }

    public void markAsLost() {
        if (!returned) {
            this.lost = true;
            this.status = "Lost";
            loanHistory.add("Book marked as lost on " + LocalDate.now());
        }
    }

    public boolean canBorrowMoreBooks(int currentLoans) {
        final int MAX_LOANS = 5;
        return currentLoans < MAX_LOANS;
    }

    public void autoRenewLoan() {
        int renewDays = loanDuration / 2;
        if (renewals < MAX_RENEWALS && !isOverdue() && !returned) {
            extendLoan(renewDays);
            renewals++;
            loanHistory.add("Auto-renewed loan by " + renewDays + " days on " + LocalDate.now());
            System.out.println("New due date after auto-renew: " + dueDate);
        } else {
            System.out.println("Cannot auto-renew loan. Maximum renewals reached or loan is overdue/returned.");
        }
    }

    public void resetLoan() {
        this.returned = false;
        this.lost = false;
        this.renewals = 0;
        this.dueDate = startDate.plusDays(this.loanDuration);
        this.status = "Active";
        loanHistory.add("Loan reset on " + LocalDate.now());
    }

    public void setDueDate(LocalDate dueDate) {this.dueDate = dueDate;
    }
}
