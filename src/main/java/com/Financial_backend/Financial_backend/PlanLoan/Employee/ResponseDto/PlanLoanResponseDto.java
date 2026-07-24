package com.Financial_backend.Financial_backend.PlanLoan.Employee.ResponseDto;

import com.Financial_backend.Financial_backend.PlanLoan.Employee.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanLoanResponseDto {


    private String id;

    private String loanPurpose;

    private Double loanAmount;

    private String repaymentTerm;

    private String loanId;

    private LoanStatus status;

    private String requestedTime;


}
