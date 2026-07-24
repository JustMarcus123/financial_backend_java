package com.Financial_backend.Financial_backend.PlanLoan;


import com.Financial_backend.Financial_backend.Entity.SponsorEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.PlanLoan.Employee.LoanStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan_request")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PlanLoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loanId;

    @Column(nullable = false)
    private String loanPurpose;

    @Column(nullable = false)
    private Double loanAmount;

    @Column(nullable = false)
    private String repaymentTerm;

    //database mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sponsor")
    private SponsorEntity sponsor;

    //loan status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;


    @CreationTimestamp
    private LocalDateTime requestedTime;
    @UpdateTimestamp
    private LocalDateTime updatedTime;
}

