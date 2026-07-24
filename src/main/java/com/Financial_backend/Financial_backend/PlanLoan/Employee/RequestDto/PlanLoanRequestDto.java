package com.Financial_backend.Financial_backend.PlanLoan.Employee.RequestDto;

import lombok.Data;

@Data
public class PlanLoanRequestDto {

    private String loanPurpose;

    private Double loanAmount;

    private String repaymentTerm;


}
