package com.example.youcandoit.repository;

import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.entity.GroupPersonEntity;
import com.example.youcandoit.entity.Id.GroupPersonId;
import com.example.youcandoit.entity.MemberEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface GroupPersonRepository extends JpaRepository<GroupPersonEntity, GroupPersonId> { // <>안에는 Entity, Primarykey를 넣어준다.
    /*
    =========================================메인페이지=========================================================
     */

    /** 그룹초대 리스트 */
    @Query(value = "select g from GroupPersonEntity p " +
            "join GroupEntity g on p.groupNumber = g.groupNumber " +
            "where p.memId=:loginId " +
            "and p.personStatus=3 " +
            "order by g.groupStartdate, g.groupName")
    List<GroupEntity> findGroupInvite(@Param("loginId")String loginId);

    /** 그룹에 초대된 멤버 */
    @Query(value = "select m.nickname, p.personStatus from GroupPersonEntity p " +
            "join MemberEntity m on p.memId = m.memId " +
            "where p.groupNumber=:groupNumber " +
            "order by p.personStatus, m.nickname")
    List<Object[]> findInviteMember(@Param("groupNumber")int groupNumber);

    /*
    =========================================자정에 동작하는 데이터베이스 업데이트=========================================================
     */

    /** 대기자 삭제 */
    @Modifying
    @Transactional
    @Query("delete from GroupPersonEntity p " +
            "where p.groupNumber in (select g.groupNumber from GroupEntity g where g.groupStartdate=:date) " +
            "and p.personStatus=3")
    void deleteGroupPerson(@Param("date")Date date);
}
