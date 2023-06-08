package com.server.streaming.domain.member;

import com.server.streaming.domain.BaseEntity;
import com.server.streaming.domain.enums.ProviderStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "member_user_id_index", columnList = "userId")
})
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String password;
    private String userId;

    private String registrationId;

    private String registerId;
    private String username;

    private String picture;

    @Enumerated(EnumType.STRING)
    private ProviderStatus providerStatus;

    @Builder
    public Member(String email, String password, String userId, String registrationId, String registerId, String username, String picture, ProviderStatus providerStatus) {
        this.email = email;
        this.password = password;
        this.registrationId = registrationId;
        this.registerId = registerId;
        this.username = username;
        this.userId = userId;
        this.picture = picture;
        this.providerStatus = providerStatus;
    }
}
