package com.Financial_backend.Financial_backend.Entity;

import com.Financial_backend.Financial_backend.Enum.BatchStatus;
import com.Financial_backend.Financial_backend.Enum.PayrollType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contribute_batch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContributionBatchEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /// this decides which company the payrole belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sponsor_id", nullable = false)
    private SponsorEntity sponsor;

    @Column(nullable = false)
    private String batchCode;  //BATCH-0315-ACME

    @Column(nullable = false)
    private String payPeriod;  //to know if it's paid monthly or weekly

    @Column(nullable = false)
    private String payDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayrollType payrollType; //REGULAR_PAYROLL, BONUS_PAYROLL, OFF_CYCLE

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BatchStatus batchStatus; //PENDING,PROCESSING, POSTED,FAILED


    private Integer totalParticipant;   //1320
    private Double totalEmployeeAmount;  //62781.0
    private Double totalEmployerMatch;  //23899.0
    private Double totalAmount;         //700200.0

    private String failureReason;      //if status = failed

    //who uploaded it
    private String uploadedBy;         //he@admin.com

    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


//    one batch has many line items(one per employee)
    @OneToMany(mappedBy = "batch",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @Builder.Default

    private List<ContributionLineItemEntity> lineItems = new ArrayList<>();
}

