package com.Financial_backend.Financial_backend.Dto.Request;

import lombok.Data;

@Data
public class EmployeeRequestDto {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    private String department;
    private String jobTitle;
    private String annualSalary;
    private String startDate;

    private Double deferralRate;

}
