package com.example.youcandoit.repository;


import com.example.youcandoit.entity.GodlifeChallengeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GodLifeChallengeRepository extends JpaRepository<GodlifeChallengeEntity, String> {
}
