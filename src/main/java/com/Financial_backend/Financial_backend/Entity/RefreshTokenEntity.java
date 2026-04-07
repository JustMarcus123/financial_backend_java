package com.Financial_backend.Financial_backend.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //the actual refresh token string (uuid)
    @Column(nullable = false, unique = true, length = 512)
    private String token;

    // this checks which category the users belong to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private UsersEntity users;

    //when this refresh token expires(eg: 7 days from creation)
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    //Soft revocation flag - set to true to immediately invalidate
    //without deleting the row(useful for audit trails)
    @Column(nullable = false)
    private Boolean revoked=false;

    //Device/ browser info - helps user see "Session " list
    @Column
    private String deviceInfo;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
