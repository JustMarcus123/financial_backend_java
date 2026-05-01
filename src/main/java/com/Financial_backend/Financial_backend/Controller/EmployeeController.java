package com.Financial_backend.Financial_backend.Controller;

import com.Financial_backend.Financial_backend.Dto.Request.EmployeeRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.EmployeeResponseDto;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.Service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
@AllArgsConstructor
@PreAuthorize("role('EMPLOYER_ADMIN')")
public class EmployeeController {


    private final EmployeeService employeeService;

    //add single employee
    @PostMapping("/add")
    public ResponseEntity<EmployeeResponseDto> addEmployee(@RequestBody EmployeeRequestDto employeeRequestDto, @AuthenticationPrincipal UsersEntity loggedInUser){

        EmployeeResponseDto employeeResponseDto = employeeService.addEmployee(
                employeeRequestDto, loggedInUser.getSponsor()
        );

        return ResponseEntity.ok(employeeResponseDto);

    }
}
