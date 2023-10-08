package com.example.youcandoit.repository;

import com.example.youcandoit.dto.DiyCountDto;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface StickerRepository extends JpaRepository<StickerEntity, StickerId> {

    /** diy 챌린지를 진행중인 사용자와 그 개수 */
    @Query(value = "select p.memId as memId, count(p) as count from GroupPersonEntity p " +
            "join GroupEntity g on p.groupNumber = g.groupNumber " +
            "where g.groupState=2 and g.groupClass = 2" +
            "group by p.memId")
    List<DiyCountDto> findDiyCount();

    /** 만보기 목표 달성한 사용자 조회 */
    @Query(value = "select m.memId from PedometerEntity p "+
            "join MemberEntity m on p.memId = m.memId " +
            "join GodlifeGoalEntity l on l.memId = m.memId " +
            "where p.pedometerDate=:date and p.pedometerResult>l.goalPedometer")
    List<String> findPedometerResult(@Param("date")Date date);

    /** diy 챌린지 달성 현황 */
    @Query(value = "select count(c) from DiyCertifyEntity c where certifyDate=:date and memId=:memId")
    Optional<Integer> findDiySuccess(@Param("date")Date date, @Param("memId")String memId);

    /** 캘린더 스티커 날짜, 컬러 */
    @Query(value = "select s from StickerEntity s " +
            "where s.memId=:loginId and s.stickerDate between :sDate and :eDate " +
            "order by s.stickerDate")
    List<StickerEntity> findStickerDateColor(@Param("loginId")String loginId, @Param("sDate")LocalDate sDate, @Param("eDate")LocalDate eDate);

}
