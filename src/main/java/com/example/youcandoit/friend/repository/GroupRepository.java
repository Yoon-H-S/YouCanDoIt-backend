package com.example.youcandoit.friend.repository;


import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<GroupEntity, Integer> { // <>안에는 Entity, Primarykey를 넣어준다.
    // 그룹 목록
    @Query(value = "select g from GroupEntity g join GroupPersonEntity p on g.groupNumber = p.groupNumber where p.memId=:loginId")
    List<GroupEntity> findGroupList(@Param("loginId")String loginId);

    // 그룹 목록 검색
    @Query(value = "select g from GroupEntity g join GroupPersonEntity p on g.groupNumber = p.groupNumber where p.memId=:loginId and g.groupName like %:groupName%")
    List<GroupEntity> findSearchGroups(@Param("loginId")String loginId, @Param("groupName")String groupName);

    // 그룹 멤버
    @Query(value = "select m from GroupPersonEntity p " +
            "join MemberEntity m on p.memId = m.memId " +
            "where p.groupNumber=:groupNumber order by p.personStatus")
    List<MemberEntity> findGroupMember(@Param("groupNumber")int groupNumber);
}
