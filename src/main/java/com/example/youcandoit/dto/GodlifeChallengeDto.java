package com.example.youcandoit.dto;

import com.example.youcandoit.entity.GodlifeChallengeEntity;
import com.example.youcandoit.entity.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GodlifeChallengeDto {
    private String challengeSubject;
    private String challengeImage;
    private String challengeContents;
    private String challengeCategory;

    public GodlifeChallengeEntity toEntity() {
        return GodlifeChallengeEntity.builder()
                .challengeSubject(challengeSubject)
                .challengeImage(challengeImage)
                .challengeContents(challengeContents)
                .challengeCategory(challengeCategory)
                .build();
    }
}
