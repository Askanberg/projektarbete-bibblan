package org.bibblan.feemanagement;

import org.bibblan.loanmanagement.Loan;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Fee {

    private double totalFees;
    private List<String> feeHistory;

    public Fee() {
        this.totalFees = 0;
        this.feeHistory = new ArrayList<>();
    }

    public void addFee(double amount) {
        this.totalFees += amount;
        feeHistory.add("Added fee of " + amount + "on " + LocalDate.now());
    }

    public double calculateFine(Loan loan) {
        return loan.calculateFine();
    }
}
