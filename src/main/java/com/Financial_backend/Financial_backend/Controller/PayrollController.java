package com.Financial_backend.Financial_backend.Controller;


import com.Financial_backend.Financial_backend.Dto.Request.PayrollUploadRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.EmployeeResponseDto;
import com.Financial_backend.Financial_backend.Dto.Response.PayrollResponseDto;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.Service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
@PreAuthorize("hasRole('EMPLOYER_ADMIN')")
public class PayrollController {

    private final PayrollService payrollService;

    //csv payroll upload
    @PostMapping("/upload")
    public ResponseEntity<PayrollResponseDto> uploadPayroll(
            @RequestParam ("file")MultipartFile file,
            @RequestParam ("payPeriod") String payPeriod,
            @RequestParam("payDate") String payDate,
            @RequestParam ("payrollType") String payrollType,
            @AuthenticationPrincipal UsersEntity loggedInUser
            ){

        PayrollUploadRequestDto requestDto =PayrollUploadRequestDto.builder()
                .payPeriod(payPeriod)
                .payDate(payDate)
                .payrollType(payrollType)
                .build();

        return ResponseEntity.ok(payrollService.payrollUpload(
                file, requestDto, loggedInUser
        ));

    }


    @GetMapping ("/fetchBalance")
    public ResponseEntity<EmployeeResponseDto> getEmployeeBalance(
            @AuthenticationPrincipal UsersEntity loggedInUser
    ){
        return ResponseEntity.ok(
                payrollService.find401kBalance(loggedInUser)
        );
    }


}
