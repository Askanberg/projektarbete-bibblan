package org.bibblan.loanmanagement;


import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;


import lombok.Data;
import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.bookcatalog.domain.Item;


@Data
public class Loan {

    private static final double DAILY_FINE_RATE = 10.0;
    private Book item;
    public LocalDate startDate;
    private LocalDate dueDate;
    private int loanDuration;
    private boolean returned = false;


    public Loan(Book book) {
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
        } else if (isOverdue()){
            return "Overdue";
        } else {
            return "Active";
        }
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
