package com.Financial_backend.Financial_backend.PlanLoan.Employer;

import com.Financial_backend.Financial_backend.PlanLoan.Employer.ResponseDto.LoanRequestResponseDto;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loanrequest")
@AllArgsConstructor
@PreAuthorize("hasRole('EMPLOYER_ADMIN')")
public class LoanRequestController {

    private  final LoanRequestService loanRequestService;

    @GetMapping("/fetchLoanRequest")
    public ResponseEntity<List<LoanRequestResponseDto>> fetchLoanRequest(@AuthenticationPrincipal UsersEntity loggedInUser){

       List<LoanRequestResponseDto>  responseDto = loanRequestService.fetchLoanRequest(
                loggedInUser
        );

        return ResponseEntity.ok(responseDto);

    }


}
