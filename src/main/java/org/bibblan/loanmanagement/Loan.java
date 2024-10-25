package org.bibblan.loanmanagement;


import java.time.LocalDate;


import lombok.Data;
import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.bookcatalog.domain.Item;


@Data
public class Loan{


    private Book item;
    private LocalDate startDate;
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
}
