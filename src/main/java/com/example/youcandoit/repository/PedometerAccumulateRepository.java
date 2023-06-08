package com.example.youcandoit.repository;

import com.example.youcandoit.entity.Id.PedometerAccumulateId;
import com.example.youcandoit.entity.Id.PedometerRankingId;
import com.example.youcandoit.entity.PedometerAccumulateEntity;
import com.example.youcandoit.entity.PedometerRankingEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface PedometerAccumulateRepository extends JpaRepository<PedometerAccumulateEntity, PedometerAccumulateId> { // <>안에는 Entity, Primarykey를 넣어준다.
    // 누적랭킹 업데이트
    @Transactional
    @Modifying // 위 2개의 어노테이션은 update/delete할 때 꼭 필요하다.
    @Query(value = "update PedometerAccumulateEntity set pedometerCount=pedometerCount+:result " +
            "where groupNumber=:groupNumber and memId=:memId")
    void updateAccumulate(@Param("result")int result, @Param("groupNumber")int groupNumber, @Param("memId")String memId);

    // 진행중인 그룹의 누적랭킹 불러오기
    @Query(value = "select a from GroupPersonEntity p " +
            "join GroupEntity g on g.groupNumber = p.groupNumber " +
            "join PedometerAccumulateEntity a on a.groupNumber = p.groupNumber " +
            "where g.groupState=2 order by a.groupNumber, a.pedometerCount desc")
    List<PedometerAccumulateEntity> findAccumulate();

    // 시작한 그룹 누적랭킹 생성
    @Modifying
    @Transactional
    @Query("insert into PedometerAccumulateEntity (groupNumber, memId) " +
            "select p.groupNumber, p.memId from GroupPersonEntity p " +
            "join GroupEntity g on g.groupNumber = p.groupNumber " +
            "where g.groupClass=1 and g.groupStartdate=:date")
    void insertAccumulate(@Param("date")Date date);
}
