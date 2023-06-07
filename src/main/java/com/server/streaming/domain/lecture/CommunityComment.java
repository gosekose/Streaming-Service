package com.server.streaming.domain.lecture;

import com.server.streaming.domain.BaseEntity;
import com.server.streaming.domain.member.Member;
import com.server.streaming.domain.lecture.enums.PublicStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class CommunityComment extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "community_answer_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @Enumerated(EnumType.STRING)
    private PublicStatus publicStatus;

    @Lob
    private String content;

    @Builder
    public CommunityComment(Community community, Member writer, PublicStatus publicStatus, String content) {
        this.community = community;
        this.writer = writer;
        this.publicStatus = publicStatus;
        this.content = content;
    }
}
