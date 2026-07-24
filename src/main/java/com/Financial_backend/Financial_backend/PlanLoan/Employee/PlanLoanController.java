package com.Financial_backend.Financial_backend.PlanLoan.Employee;


import com.Financial_backend.Financial_backend.PlanLoan.Employee.RequestDto.PlanLoanRequestDto;
import com.Financial_backend.Financial_backend.PlanLoan.Employee.ResponseDto.PlanLoanResponseDto;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loanrequest")
@AllArgsConstructor
@PreAuthorize("hasRole(EMPLOYEE)")
public class PlanLoanController {

    private final  PlanLoanService planLoanService;

    @PostMapping("/newRequest")
    public ResponseEntity<PlanLoanResponseDto> LoanRequest(@RequestBody PlanLoanRequestDto loanRequestDto,
                                                           @AuthenticationPrincipal UsersEntity loggedInUser
                                                           ){

        PlanLoanResponseDto planLoanResponseDto = planLoanService.requestNewLoan(
                loanRequestDto, loggedInUser.getSponsor(), loggedInUser
        );

        return ResponseEntity.ok(planLoanResponseDto);

    }

    @GetMapping("/fetchLoanStatus")

    public ResponseEntity<List<PlanLoanResponseDto>> fetchLoanStatus(@AuthenticationPrincipal UsersEntity loggedInUser){


        return ResponseEntity.ok(planLoanService.fetchLoanStatus(loggedInUser.getSponsor() , loggedInUser));

    }

}
