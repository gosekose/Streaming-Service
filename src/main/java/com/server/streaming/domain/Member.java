package com.server.streaming.domain;

import com.server.streaming.domain.enums.ProviderStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String password;
    private String userId;

    @Enumerated(EnumType.STRING)
    private ProviderStatus providerStatus;

    @Builder
    public Member(String email, String password, ProviderStatus providerStatus) {
        this.email = email;
        this.password = password;
        this.userId = UUID.randomUUID().toString();
        this.providerStatus = providerStatus;
    }
}
