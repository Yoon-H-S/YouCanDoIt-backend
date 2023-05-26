package com.example.youcandoit.entity;

import com.example.youcandoit.dto.GodlifeChallengeDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "challenge_group") // db의 해당 테이블과 연결
@DynamicUpdate
public class GodlifeChallengeEntity {
    @Id
    @Column(name = "challenge_subject")
    private String challengeSubject;
    @Column(name = "challenge_image")
    private String challengeImage;
    @Column(name = "challenge_contents")
    private String challengeContents;
    @Column(name = "challenge_category")
    private String challengeCategory;

    public GodlifeChallengeDto toDto() {
        return GodlifeChallengeDto.builder()
                .challengeSubject(challengeSubject)
                .challengeImage(challengeImage)
                .challengeContents(challengeContents)
                .challengeCategory(challengeCategory)
                .build();
    }
}
