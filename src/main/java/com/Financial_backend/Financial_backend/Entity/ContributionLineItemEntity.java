package com.Financial_backend.Financial_backend.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contribution_line_item")
public class ContributionLineItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //which batch this line belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private ContributionBatchEntity batch;

    //Which employee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private UsersEntity employee;

    private String employeeCode;     // EMP-00182
    private Double grossSalary;      // 5833.33
    private Double deferralRate;     // 0.08
    private Double employeeAmount;   // 466.67
    private Double employerMatch;    // 233.33
    private Double totalAmount;      // 700.00
    private String payDate;

    private Boolean irsLimitReached; // true if employee hit $23,000
    private Boolean posted;          // true = balance updated, false in case if it fails
    private String failureReason;    // if this line failed


}
