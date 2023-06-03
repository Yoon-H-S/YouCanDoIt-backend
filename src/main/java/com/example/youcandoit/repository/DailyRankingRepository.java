package com.example.youcandoit.repository;

import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.entity.Id.PedometerRankingId;
import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.entity.PedometerRankingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DailyRankingRepository extends JpaRepository<PedometerRankingEntity, PedometerRankingId> {

}
