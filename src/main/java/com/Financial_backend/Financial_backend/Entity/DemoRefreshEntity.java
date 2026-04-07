package com.Financial_backend.Financial_backend.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "RefreshDemo")
public class DemoRefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // the actual refresh token

    @Column(nullable = false, unique = true, length = 512)
    private String refreshTokenDemo;

    //join the table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
   private UsersEntity users;

    //Storing the device info

    private String Device_info;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    //tracking the last login
    private String lastLogin;    // this will be set only when logic
}
