package com.Financial_backend.Financial_backend.Entity;

import com.Financial_backend.Financial_backend.Enum.EmployeeStatus;
import com.Financial_backend.Financial_backend.Enum.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")

public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password_hash;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true)
    private String phone;





    // ── CORRECT ROLE FIELD ──
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;          // PARTICIPANT, EMPLOYER_ADMIN, SUPER_ADMIN


    //401k specific field
    private String employeeId;   //EMP-00182
    private String department;    //IT
    private String jobTitle;      //software engineer
    private String annualSalary;  //
    private String deferralRate;  // 0.8 = 8%
    private String startDate;

    private EmployeeStatus status;   //ACTIVE, NOT_ENROLLED, PENDING, TERMINATED


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sponsor_id")
    private SponsorEntity sponsor;

    @Column(nullable = false)
    private Boolean mfa_enable = false;
    private String mfa_secret;
    @Column(nullable = false)
    private Boolean is_active = false;     // Better naming: isActive
    private Boolean isTempPassword = true;
    @Column(nullable = false)
    private Boolean email_varified = false; // Typo: should be email_verified

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    private String last_login_at;

}
