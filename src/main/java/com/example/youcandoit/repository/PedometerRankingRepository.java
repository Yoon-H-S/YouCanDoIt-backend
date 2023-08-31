package com.example.youcandoit.repository;

import com.example.youcandoit.entity.GroupPersonEntity;
import com.example.youcandoit.entity.Id.PedometerRankingId;
import com.example.youcandoit.entity.PedometerRankingEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface PedometerRankingRepository extends JpaRepository<PedometerRankingEntity, PedometerRankingId> { // <>안에는 Entity, Primarykey를 넣어준다.

    /*
    =========================================챌린지페이지=========================================================
     */

    /** 갓생 챌린지 일일랭킹 리스트의 순위 */
    @Query(value = "select m.profilePicture, r.pedometerResult from PedometerRankingEntity r " +
            "join GroupPersonEntity p on r.groupNumber = p.groupNumber and r.memId = p.memId " +
            "join MemberEntity m on m.memId = p.memId " +
            "where p.groupNumber=:groupNumber and r.pedometerDate=:date " +
            "order by r.pedometerRank limit 3")
    List<Object[]> findDailyRanking(@Param("groupNumber")int groupNumber, @Param("date")Date date);

    /** 갓생 챌린지 일일랭킹 상세의 순위 */
    @Query(value = "select m.nickname, m.profilePicture, r.pedometerResult, r.pedometerRank from PedometerRankingEntity r " +
            "join GroupPersonEntity p on r.groupNumber = p.groupNumber and r.memId = p.memId " +
            "join MemberEntity m on m.memId = p.memId " +
            "where p.groupNumber=:groupNumber and r.pedometerDate=:date " +
            "order by r.pedometerRank")
    List<Object[]> findDailyRankingDetail(@Param("groupNumber")int groupNumber, @Param("date")Date date);

    /**
    =========================================메인페이지=========================================================
     */

    /** 나의랭킹 */
    @Query(value = "select g, r.pedometerRank from PedometerRankingEntity r " +
            "join GroupPersonEntity p on r.groupNumber = p.groupNumber and r.memId = p.memId " +
            "join GroupEntity g on p.groupNumber = g.groupNumber " +
            "where r.pedometerDate=:date and r.memId=:loginId and g.groupState=2 " +
            "order by groupEnddate")
    List<Object[]> findMyRankingList(@Param("loginId")String loginId, @Param("date")Date date);

    /*
    =========================================자정에 동작하는 데이터베이스 업데이트=========================================================
     */

    /** 오늘의 일일랭킹 리스트 조회 */
    List<PedometerRankingEntity> findByPedometerDateOrderByGroupNumberAscPedometerResultDesc(Date pedometerDate);

    /** 시작한 그룹 일일랭킹 생성 */
    @Modifying
    @Transactional
    @Query("insert into PedometerRankingEntity (pedometerDate, groupNumber, memId) " +
            "select :date, p.groupNumber, p.memId from GroupPersonEntity p " +
            "join GroupEntity g on g.groupNumber = p.groupNumber " +
            "where g.groupClass=1 and g.groupState=2 ")
    void insertRanking(@Param("date")Date date);
}
