package org.bibblan.loanmanagement;

import java.time.LocalDate;
import org.bibblan.bookcatalog.Author;
import org.bibblan.bookcatalog.Book;
import org.bibblan.bookcatalog.CoverType;

private LocalDate startDate;
private LocalDate dueDate;
private boolean returned;


@Data
public class Loan {

    public Loan(Item book){
        this.item = book;
        this.startDate = LocalDate.now();
        this.dueDate = startDate.plusDays(item.getLoanDuration());
    }

    public void returnBook(){
        this.returned = true;
    }
}
