package com.example.youcandoit.repository;

import com.example.youcandoit.entity.DiyAccumulateEntity;
import com.example.youcandoit.entity.Id.DiyAccumulateId;
import com.example.youcandoit.entity.PedometerAccumulateEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface DiyAccumulateRepository extends JpaRepository<DiyAccumulateEntity, DiyAccumulateId> {

    /** diy 챌린지 리스트의 순위 */
    @Query(value = "select m.profilePicture, a.certifyCount from GroupPersonEntity p " +
            "join DiyAccumulateEntity a on a.groupNumber = p.groupNumber and a.memId = p.memId " +
            "join MemberEntity m on m.memId = p.memId " +
            "where p.groupNumber=:groupNumber " +
            "order by a.diyaccuRank limit 3")
    List<Object[]> findDiyRanking(@Param("groupNumber")int groupNumber);

    /** DIY 챌린지 랭킹 상세의 순위 */
    @Query(value = "select m.nickname, m.profilePicture, a.certifyCount, a.diyaccuRank, m.memId from DiyAccumulateEntity a " +
            "join GroupPersonEntity p on a.groupNumber = p.groupNumber and a.memId = p.memId " +
            "join MemberEntity m on m.memId = p.memId " +
            "where p.groupNumber=:groupNumber " +
            "order by a.diyaccuRank")
    List<Object[]> findDiyRankingDetail(@Param("groupNumber")int groupNumber);

    /*
    =========================================자정에 동작하는 데이터베이스 업데이트=========================================================
     */

    /** 인증 수 감소 */
    @Transactional
    @Modifying
    @Query(value = "update DiyAccumulateEntity set certifyCount=certifyCount-1 " +
            "where groupNumber=:groupNumber and memId=:memId")
    void decreaseCertifyCount(@Param("groupNumber")int groupNumber, @Param("memId")String memId);

    /** 인증 랭킹에 반영 */
    @Transactional
    @Modifying
    @Query(value = "update DiyAccumulateEntity set certifyCount=certifyCount+1 " +
            "where groupNumber=:groupNumber and memId=:memId")
    void increaseCertifyCount(@Param("groupNumber")int groupNumber, @Param("memId")String memId);

    /** 진행중인 Diy 그룹의 랭킹 리스트 */
    @Query(value = "select a from DiyAccumulateEntity a " +
            "join GroupPersonEntity p on p.groupNumber = a.groupNumber and p.memId = a.memId  " +
            "join GroupEntity g on g.groupNumber = p.groupNumber " +
            "where g.groupState=2 order by a.groupNumber, a.certifyCount desc")
    List<DiyAccumulateEntity> findDiyAccumulate();

    /** 시작한 그룹 랭킹 데이터 생성 */
    @Modifying
    @Transactional
    @Query("insert into DiyAccumulateEntity (groupNumber, memId) " +
            "select p.groupNumber, p.memId from GroupPersonEntity p " +
            "join GroupEntity g on g.groupNumber = p.groupNumber " +
            "where g.groupClass=2 and g.groupStartdate=:date")
    void insertDiyAccumulate(@Param("date") Date date);

}
