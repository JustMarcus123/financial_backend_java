package com.Financial_backend.Financial_backend.Dto.Request;


import com.Financial_backend.Financial_backend.Enum.BatchStatus;
import com.Financial_backend.Financial_backend.Enum.PayrollType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ContributionBatchEntityDto {

    private String sponsor;
    private String batchCode;
    private String payPeriod;
    private String payDate;
    private PayrollType payrollType; //REGULAR_PAYROLL, BONUS_PAYROLL, OFF_CYCLE
    private BatchStatus batchStatus; //PENDING,PROCESSING, POSTED,FAILED
    private Integer totalParticipant;   //1320
    private Double totalEmployeeAmount;  //62781.0
    private Double totalEmployerMatch;  //23899.0
    private Double totalAmount;         //700200.0
    private String uploadedBy;
}
