package com.example.youcandoit.challenge.repository;


import com.example.youcandoit.entity.GodlifeChallengeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GodLifeChallengeRepository extends JpaRepository<GodlifeChallengeEntity, String> {
    Optional<GodlifeChallengeEntity> findByChallengeSubjectAndChallengeImageAndChallengeContentsAndChallengeCategory(String challengeSubject, String challengeImage, String challengeContents, String challengeCategory);

    Optional<GodlifeChallengeEntity> findByChallengeSubjectAndChallengeContents(String challengeSubject, String challengeContents);
}
