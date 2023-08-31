package com.example.youcandoit.repository;

import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.entity.MemberEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface GroupRepository extends JpaRepository<GroupEntity, Integer> { // <>안에는 Entity, Primarykey를 넣어준다.
    /*
    =========================================그룹페이지=========================================================
     */

    /** 그룹 목록 */
    @Query(value = "select g from GroupEntity g " +
            "join GroupPersonEntity p on g.groupNumber = p.groupNumber " +
            "where p.memId=:loginId and (p.personStatus=1 or p.personStatus=2) " +
            "order by FIELD(g.groupState, 2, 1, 3), g.groupEnddate")
    List<GroupEntity> findGroupList(@Param("loginId")String loginId);

    /** 그룹 목록 검색 */
    @Query(value = "select g from GroupEntity g " +
            "join GroupPersonEntity p on g.groupNumber = p.groupNumber " +
            "where p.memId=:loginId and g.groupName like %:groupName% and (p.personStatus=1 or p.personStatus=2) " +
            "order by FIELD(g.groupState, 2, 1, 3), g.groupEnddate")
    List<GroupEntity> findSearchGroups(@Param("loginId")String loginId, @Param("groupName")String groupName);

     /** 그룹 프로필사진 */
    @Query(value = "select m.profilePicture from GroupPersonEntity p " +
            "join MemberEntity m on p.memId = m.memId " +
            "where p.groupNumber=:groupNumber and (p.personStatus=1 or p.personStatus=2) " +
            "order by p.personStatus, m.nickname limit 3")
    List<String> findProfilePicture(@Param("groupNumber")int groupNumber);

    /** 그룹 멤버 */
    @Query(value = "select m from GroupPersonEntity p " +
            "join MemberEntity m on p.memId = m.memId " +
            "where p.groupNumber=:groupNumber and (p.personStatus=1 or p.personStatus=2) " +
            "order by p.personStatus, m.nickname")
    List<MemberEntity> findGroupMember(@Param("groupNumber")int groupNumber);

    /*
    =========================================메인페이지=========================================================
     */

    /** 그룹멤버 증가 */
    @Transactional
    @Modifying
    @Query(value = "update GroupEntity set groupHeadcount=groupHeadcount+1 " +
            "where groupNumber=:groupNumber")
    void updateGroupMember(@Param("groupNumber")int groupNumber);

    /*
    =========================================챌린지페이지=========================================================
     */

    /** 챌린지 그룹 조회(랭킹페이지) */
    @Query("select g from GroupEntity g " +
            "join GroupPersonEntity p on p.groupNumber = g.groupNumber " +
            "where p.memId=:loginId and g.groupClass=:groupClass and g.groupState=2 " +
            "order by g.groupEnddate")
    List<GroupEntity> findInProgressGroupList(@Param("loginId")String loginId, @Param("groupClass")String groupClass);

    @Query("select g from GroupEntity g " +
            "join GroupPersonEntity p on p.groupNumber = g.groupNumber " +
            "where p.memId=:loginId and g.groupClass in :groupClass and g.groupState=3 " +
            "order by g.groupEnddate desc")
    List<GroupEntity> findEndGroupList(@Param("loginId")String loginId, @Param("groupClass")String[] groupClass);

    /** 예약된 챌린지 리스트 */
    @Query(value = "select g from GroupEntity g " +
            "join GroupPersonEntity p on p.groupNumber = g.groupNumber " +
            "where p.memId=:loginId and g.groupState=1 and (p.personStatus=1 or p.personStatus=2)" +
            "order by g.groupStartdate")
    List<GroupEntity> findChallengeReservation(@Param("loginId")String loginId);

    /*
    =========================================자정에 동작하는 데이터베이스 업데이트=========================================================
     */

    /** 그룹 종료 */
    @Transactional
    @Modifying
    @Query(value = "update GroupEntity set groupState='3' where groupEnddate=:date")
    void updateGroupEnd(@Param("date")Date date);

    /** 그룹 시작 */
    @Transactional
    @Modifying
    @Query(value = "update GroupEntity set groupState='2' where groupStartdate=:date")
    void updateGroupStart(@Param("date")Date date);
}
