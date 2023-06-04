package com.example.youcandoit.repository;

import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface GroupRepository extends JpaRepository<GroupEntity, Integer> { // <>안에는 Entity, Primarykey를 넣어준다.
    // 그룹 목록
    @Query(value = "select g from GroupEntity g " +
            "join GroupPersonEntity p on g.groupNumber = p.groupNumber " +
            "where p.memId=:loginId and (p.personStatus=1 or p.personStatus=2) " +
            "order by FIELD(g.groupState, 2, 1, 3), g.groupEnddate")
    List<GroupEntity> findGroupList(@Param("loginId")String loginId);

    // 그룹 목록 검색
    @Query(value = "select g from GroupEntity g " +
            "join GroupPersonEntity p on g.groupNumber = p.groupNumber " +
            "where p.memId=:loginId and g.groupName like %:groupName% and (p.personStatus=1 or p.personStatus=2) " +
            "order by FIELD(g.groupState, 2, 1, 3), g.groupEnddate")
    List<GroupEntity> findSearchGroups(@Param("loginId")String loginId, @Param("groupName")String groupName);

    // 그룹 프로필사진
    @Query(value = "select m.profilePicture from GroupPersonEntity p " +
            "join MemberEntity m on p.memId = m.memId " +
            "where p.groupNumber=:groupNumber and (p.personStatus=1 or p.personStatus=2) " +
            "order by p.personStatus, m.nickname limit 3")
    List<String> findProfilePicture(@Param("groupNumber")int groupNumber);

    // 그룹 멤버
    @Query(value = "select m from GroupPersonEntity p " +
            "join MemberEntity m on p.memId = m.memId " +
            "where p.groupNumber=:groupNumber and (p.personStatus=1 or p.personStatus=2) " +
            "order by p.personStatus, m.nickname")
    List<MemberEntity> findGroupMember(@Param("groupNumber")int groupNumber);

    /*
    =========================================메인페이지=========================================================
     */

    // 메인페이지 그룹초대
    @Query(value = "select g from GroupPersonEntity p " +
            "join GroupEntity g on p.groupNumber = g.groupNumber " +
            "where p.memId=:loginId and p.personStatus=3 " +
            "order by g.groupStartdate, g.groupName")
    List<GroupEntity> findGroupInvite(@Param("loginId")String loginId);

    // 그룹초대 멤버
    @Query(value = "select m.nickname, p.personStatus from GroupPersonEntity p " +
            "join MemberEntity m on p.memId = m.memId " +
            "where p.groupNumber=:groupNumber " +
            "order by p.personStatus, m.nickname")
    List<Object[]> findInviteMember(@Param("groupNumber")int groupNumber);

    /*
    =========================================챌린지=========================================================
     */

    // 진행중인 그룹
    @Query("select g from GroupEntity g " +
            "join GroupPersonEntity p on p.groupNumber = g.groupNumber " +
            "where p.memId=:loginId and g.groupClass=1 and g.groupState=2 " +
            "order by g.groupEnddate")
    List<GroupEntity> findRankingList(@Param("loginId")String loginId);

    // 일일랭킹 리스트의 순위
    @Query(value = "select m.profilePicture, r.pedometerResult from GroupPersonEntity p " +
            "join PedometerRankingEntity r on r.groupNumber = p.groupNumber and r.memId = p.memId " +
            "join MemberEntity m on m.memId = p.memId " +
            "where p.groupNumber=:groupNumber and r.pedometerDate=:date " +
            "order by r.pedometerRank limit 3")
    List<Object[]> findDailyRanking(@Param("groupNumber")int groupNumber, @Param("date") Date date);

    // 일일랭킹 상세의 순위
    @Query(value = "select m.nickname, m.profilePicture, r.pedometerResult from GroupPersonEntity p " +
            "join PedometerRankingEntity r on r.groupNumber = p.groupNumber and r.memId = p.memId " +
            "join MemberEntity m on m.memId = p.memId " +
            "where p.groupNumber=:groupNumber and r.pedometerDate=:date " +
            "order by r.pedometerRank")
    List<Object[]> findDailyRankingDetail(@Param("groupNumber")int groupNumber, @Param("date") Date date);

    // 누적랭킹 리스트의 순위
    @Query(value = "select m.profilePicture, a.pedometerCount from GroupPersonEntity p " +
            "join PedometerAccumulateEntity a on a.groupNumber = p.groupNumber and a.memId = p.memId " +
            "join MemberEntity m on m.memId = p.memId " +
            "where p.groupNumber=:groupNumber " +
            "order by a.pedoaccuRank limit 3")
    List<Object[]> findRanking(@Param("groupNumber")int groupNumber);

    // 누적랭킹 상세의 순위
    @Query(value = "select m.nickname, m.profilePicture, a.pedometerCount from GroupPersonEntity p " +
            "join PedometerAccumulateEntity a on a.groupNumber = p.groupNumber and a.memId = p.memId " +
            "join MemberEntity m on m.memId = p.memId " +
            "where p.groupNumber=:groupNumber " +
            "order by a.pedoaccuRank")
    List<Object[]> findRankingDetail(@Param("groupNumber")int groupNumber);

    // 예약된 챌린지
    @Query(value = "select g from GroupPersonEntity p " +
            "join GroupEntity g on p.groupNumber = g.groupNumber " +
            "where p.memId=:loginId and g.groupState=1 " +
            "order by g.groupStartdate")
    List<GroupEntity> findChallengeReservation(@Param("loginId")String loginId);
}
