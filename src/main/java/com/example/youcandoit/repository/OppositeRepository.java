package com.example.youcandoit.repository;

import com.example.youcandoit.entity.Id.OppositeId;
import com.example.youcandoit.entity.OppositeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OppositeRepository extends JpaRepository<OppositeEntity, OppositeId> {
}
