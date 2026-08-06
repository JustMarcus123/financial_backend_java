package com.Financial_backend.Financial_backend.PlanLoan.Employer;


import com.Financial_backend.Financial_backend.PlanLoan.Employer.Exception.SponsorNotExistException;
import com.Financial_backend.Financial_backend.PlanLoan.Employer.ResponseDto.LoanRequestResponseDto;
import com.Financial_backend.Financial_backend.Entity.SponsorEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.PlanLoan.PlanLoanEntity;
import com.Financial_backend.Financial_backend.PlanLoan.PlanLoanRepository;
import com.Financial_backend.Financial_backend.Respository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor
public class LoanRequestService {

    private final PlanLoanRepository planLoanRepository;
    private final UsersRepository usersRepository;

    //-------------fetching the loan request---------------

    public List <LoanRequestResponseDto> fetchLoanRequest(
            UsersEntity sponsor
    ){
        //first step is to check if the sponsor is available
        UsersEntity sponsors = usersRepository.findByEmail(sponsor.getEmail())
                .orElseThrow(()->new SponsorNotExistException("sponsor does not exist "));





        return planLoanRepository.findLoanRequestOfCorrespondingEmp(sponsors.getSponsor())
                .stream()
                .map(planLoanEntity -> {

                    UsersEntity employee = planLoanEntity.getUser();
                    LoanRequestResponseDto loanRequestResponseDto = LoanRequestResponseDto.builder()
                            .loanId(planLoanEntity.getLoanId())
                            .loanPurpose(planLoanEntity.getLoanPurpose())
                            .loanAmount(planLoanEntity.getLoanAmount())
                            .repaymentTerm(planLoanEntity.getRepaymentTerm())
                            .user(employee.getFirstName() + " " + employee.getLastName())
                            .requestedTime(planLoanEntity.getRequestedTime())
                            .status(planLoanEntity.getStatus())
                            .vestedBalance(employee.getBalance())
                            .maxEligible(employee.getBalance()* 0.50)
                            .monthlyPayment(planLoanEntity.getLoanAmount()*0.05 + monthlyAmount(planLoanEntity))
                            .build();

                    return loanRequestResponseDto;

                }
                ).collect(Collectors.toList());



    }

    // helper
    public double monthlyAmount(
            PlanLoanEntity usersData
    ){
        double amount = usersData.getLoanAmount() / Integer.parseInt( usersData.getRepaymentTerm());

        return amount;
    }


}
