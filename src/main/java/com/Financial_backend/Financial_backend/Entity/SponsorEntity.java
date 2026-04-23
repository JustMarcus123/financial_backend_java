package com.Financial_backend.Financial_backend.Entity;


import com.Financial_backend.Financial_backend.Enum.SponsorStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@Table(name = "sponsor")
@NoArgsConstructor
@AllArgsConstructor
public class SponsorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 225)
    private String company_name;

    @Column(unique = true, length = 50)
    private String enrollmentCode; // this will be a code foe employee self enrollment which will be created when creating the sponsor

    @Builder.Default
    @OneToMany(mappedBy ="sponsor",fetch = FetchType.LAZY)
    private List<UsersEntity>  users = new ArrayList<>();

    @Column(nullable = false)
    private String plan_type;

    @Column(nullable = false)
    private String match_formula;

    @Column(nullable = false)
    private String vesting_schedule;

    @Column(nullable = false)
    private String safe_harbour_plan;

    @Column(nullable = false)
    private String plan_start_date;

    @Column(nullable = false, unique = true, length = 50)
    private String ein;    //employer identification Number(tax ID)

    @Column(length = 100)
    private String primaryContactName;

    @Column(unique = true, nullable = false, length = 225)
    private String primaryContactEmail;

    @Column(length =20)
    private  String primary_contact_phone;

    @Column(length = 500)
    private String addressLine1;

    @Column(length = 225)
    private String city;

    @Column(length = 225)
    private String state;

    @Column(length = 20)
    private String zipcode;

    @Column(length = 100)
    private String country = "United States";   // which means by default it will keep india by default if not choose

    @Enumerated(EnumType.STRING)
    private SponsorStatus sponsorStatus = SponsorStatus.DRAFT;

    private LocalDateTime onboarded_date; // needed to change to plan start date

    //audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



}
