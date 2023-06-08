package com.example.youcandoit.repository;

import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.entity.Id.PedometerRankingId;
import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.entity.PedometerRankingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface PedometerRankingRepository extends JpaRepository<PedometerRankingEntity, PedometerRankingId> { // <>안에는 Entity, Primarykey를 넣어준다.
    List<PedometerRankingEntity> findByPedometerDateOrderByGroupNumberAscPedometerResultDesc(Date pedometerDate);
}
