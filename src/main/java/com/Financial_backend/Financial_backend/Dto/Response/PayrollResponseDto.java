package com.Financial_backend.Financial_backend.Dto.Response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PayrollResponseDto {
    
    private Long id;
    private String batchCode;
    private String payPeriod;
    private String payDate;
    private String payrollType;
    private String batchStatus;
    private Integer totalParticipant;
    private Double totalEmployeeAmount;
    private Double totalEmployerMatch;
    private Double totalAmount;
    private String  failureReason;
    private String uploadedBy;
    private String createdAt;

}
