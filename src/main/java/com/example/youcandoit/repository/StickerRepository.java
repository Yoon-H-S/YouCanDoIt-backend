package com.example.youcandoit.repository;

import com.example.youcandoit.entity.Id.StickerId;
import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.entity.StickerEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

public interface StickerRepository extends JpaRepository<StickerEntity, StickerId> {
//    /** 스티커 데이터 삽입*/
//    @Modifying
//    @Transactional
//    @Query("insert into StickerEntity(stickerDate, memId, totalCount, successCount)"+
//            "select :date, p.memId, p.count(*), :count from GroupPersonEntity p" +
//            "join GroupEntity g on p.groupNumber = g.groupNumber" +
//            "where g.groupClass = 2 and g.groupState = 2 and p.personStatus = 2")
//    void insertSticker(@Param("date")Date date, @Param("count")Integer successCount);

//    @Modifying
//    @Transactional
//    @Query("insert into StickerEntity(stickerDate, memId, totalCount, successCount)"+
//            "select :date, p.memId, p.count(*), :count from GroupPersonEntity p" +
//            "join g on p.groupNumber = g.groupNumber" +
//            "where g.groupClass = 2 and g.groupState = 2 and p.personStatus in (1, 2)")
//    void insertSticker(@Param("date")Date date, @Param("count")Integer successCount);

//    @Query("select count(*) from GroupPersonEntity gp " +
//            "join GroupEntity g on gp.groupNumber = g.groupNumber " +
//            "where gp.memId = :loginId and groupState = 2 and groupClass = 2")
//    int challengeTotalCount(@Param("loginId")String loginId);

    /** diy 조회된 애들 총 카운트 */
    @Query(value = "select g.memId, count(*) from GroupPersonEntity p " +
            "join ChallengeGroupEntity g on p.groupNumber = g.groupNumber" +
            "where g.groupState=2 and g.groupClass = 2" +
            "group by g.memId;")
    List<Objects[]> findDiyCount();

    /** 만보기 결과 및 개인 목표 조회 */
    @Query(value = "select p.pedometerResult, l.goalPedometer from PedometerEntity p "+
            "join MemberEntity m on p.memId = m.memId" +
            "join GodlifeGoalEntity l on l.memId = m.memId" +
            "where p.pedometerDate = :date")
    List<Objects[]> findPedometerResult(@Param("date")Date date);

}
