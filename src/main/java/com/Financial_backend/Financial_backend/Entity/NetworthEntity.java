package com.Financial_backend.Financial_backend.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "networth")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NetworthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private UsersEntity employee;


    @Column(nullable = false)
    private Double TotalAssets;
    @Column(nullable = false)
    private Double TotalLiabilities;
    @Column(nullable = false)
    private Double NetWorth;

    @UpdateTimestamp
    private LocalDateTime UpdatedTime;



}
