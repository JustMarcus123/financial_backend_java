package com.Financial_backend.Financial_backend.PlanLoan.Employer.ResponseDto;

import com.Financial_backend.Financial_backend.PlanLoan.Employee.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class LoanRequestResponseDto {

    private String id;

    private String loanPurpose;

    private Double loanAmount;

    private String repaymentTerm;

    private String loanId;

    private LoanStatus status;

    private String user;

    private String requestedTime;

}
