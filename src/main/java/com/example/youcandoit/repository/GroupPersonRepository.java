package com.example.youcandoit.repository;

import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.entity.GroupPersonEntity;
import com.example.youcandoit.entity.Id.GroupPersonId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface GroupPersonRepository extends JpaRepository<GroupPersonEntity, GroupPersonId> { // <>안에는 Entity, Primarykey를 넣어준다.
    // 나의랭킹
    @Query(value = "select g, r.pedometerRank from GroupPersonEntity p " +
            "join GroupEntity g on p.groupNumber = g.groupNumber " +
            "join PedometerRankingEntity r on p.groupNumber = r.groupNumber and p.memId = r.memId " +
            "where r.pedometerDate=:date and r.memId=:loginId and g.groupState=2 " +
            "order by groupEnddate")
    List<Object[]> findGroup(@Param("loginId")String loginId, @Param("date")Date date);

    /*
    =========================================자정에 동작하는 데이터베이스 업데이트=========================================================
     */

    // 그룹 시작하고 대기자 삭제
    @Modifying
    @Transactional
    @Query("delete from GroupPersonEntity p " +
            "where p.groupNumber in (select g.groupNumber from GroupEntity g where g.groupStartdate=:date) and p.personStatus=3")
    void deleteGroupPerson(@Param("date")Date date);

    // 진행중인 그룹의 멤버정보(일일랭킹 row 생성을 위함)
    @Query("select p from GroupPersonEntity p " +
            "join GroupEntity g on g.groupNumber = p.groupNumber " +
            "where g.groupClass=1 and g.groupState=2 " +
            "order by g.groupNumber")
    List<GroupPersonEntity> findInsertRanking();
}
