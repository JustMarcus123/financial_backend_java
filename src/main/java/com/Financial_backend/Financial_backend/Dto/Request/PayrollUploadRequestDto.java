package com.Financial_backend.Financial_backend.Dto.Request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayrollUploadRequestDto {

    private String payPeriod;
    private String payDate;
    private String payrollType;


}
