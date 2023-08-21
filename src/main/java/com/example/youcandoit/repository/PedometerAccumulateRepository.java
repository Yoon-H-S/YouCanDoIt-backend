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

    /*
    =========================================챌린지페이지=========================================================
     */

    /** 갓생 챌린지 누적랭킹 리스트의 순위 */
    @Query(value = "select m.profilePicture, a.pedometerCount from PedometerAccumulateEntity a " +
            "join GroupPersonEntity p on a.groupNumber = p.groupNumber and a.memId = p.memId " +
            "join MemberEntity m on m.memId = p.memId " +
            "where p.groupNumber=:groupNumber " +
            "order by a.pedoaccuRank limit 3")
    List<Object[]> findGodLifeRanking(@Param("groupNumber")int groupNumber);

    /** 갓생 챌린지 누적랭킹 상세의 순위 */
    @Query(value = "select m.nickname, m.profilePicture, a.pedometerCount, a.pedoaccuRank from PedometerAccumulateEntity a " +
            "join GroupPersonEntity p on a.groupNumber = p.groupNumber and a.memId = p.memId " +
            "join MemberEntity m on m.memId = p.memId " +
            "where p.groupNumber=:groupNumber " +
            "order by a.pedoaccuRank")
    List<Object[]> findGodLifeRankingDetail(@Param("groupNumber")int groupNumber);

    /*
    =========================================자정에 동작하는 데이터베이스 업데이트=========================================================
     */

    /** 누적랭킹 업데이트 */
    @Transactional
    @Modifying // 위 2개의 어노테이션은 update/delete할 때 꼭 필요하다.
    @Query(value = "update PedometerAccumulateEntity " +
            "set pedometerCount=pedometerCount+:result " +
            "where groupNumber=:groupNumber and memId=:memId")
    void updateAccumulate(@Param("result")int result, @Param("groupNumber")int groupNumber, @Param("memId")String memId);

    /** 진행중인 그룹의 누적랭킹 리스트 */
    @Query(value = "select a from PedometerAccumulateEntity a " +
            "join GroupPersonEntity p on p.groupNumber = a.groupNumber " +
            "join GroupEntity g on g.groupNumber = p.groupNumber " +
            "where g.groupState=2 order by a.groupNumber, a.pedometerCount desc")
    List<PedometerAccumulateEntity> findAccumulate();

    /** 시작한 그룹 누적랭킹 데이터 생성 */
    @Modifying
    @Transactional
    @Query("insert into PedometerAccumulateEntity (groupNumber, memId) " +
            "select p.groupNumber, p.memId from GroupPersonEntity p " +
            "join GroupEntity g on g.groupNumber = p.groupNumber " +
            "where g.groupClass=1 and g.groupStartdate=:date")
    void insertAccumulate(@Param("date")Date date);
}
