package com.Financial_backend.Financial_backend.Dto.Response;

import com.Financial_backend.Financial_backend.Enum.EmployeeStatus;
import lombok.Data;

@Data
public class EmployeeResponseDto {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    private String department;
    private String jobTitle;
    private String annualSalary;
    private String employeeId;   //EMP-00182
    private EmployeeStatus status;   //ACTIVE, NOT_ENROLLED, PENDING, TERMINATED

    private String startDate;
    private Double deferralRate;


}
