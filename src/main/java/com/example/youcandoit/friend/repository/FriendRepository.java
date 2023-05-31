package com.example.youcandoit.friend.repository;


import com.example.youcandoit.entity.FriendEntity;
import com.example.youcandoit.entity.Id.FriendId;
import com.example.youcandoit.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<FriendEntity, FriendId> { // <>안에는 Entity, Primarykey를 넣어준다.
    // 친구 목록
    @Query(value = "select m from FriendEntity f join MemberEntity m on f.friendId = m.memId where f.memId=:loginId order by m.nickname")
    List<MemberEntity> findFriendList(@Param("loginId")String loginId);

    // 친구 목록 검색
    @Query(value = "select m from FriendEntity f join MemberEntity m on f.friendId = m.memId where f.memId=:loginId and m.nickname like %:friendName% order by m.nickname")
    List<MemberEntity> findSearchFriends(@Param("loginId")String loginId, @Param("friendName")String friendName);

    // 친구 상세 프로필(같이 있는 그룹)
    @Query(value = "select g.groupNumber, g.groupName from GroupPersonEntity p1 " +
            "join GroupPersonEntity p2 on p1.groupNumber = p2.groupNumber join GroupEntity g on p1.groupNumber = g.groupNumber " +
            "where p1.memId=:memId and p2.memId=:friendId and (p1.personStatus=1 or p1.personStatus=2) and (p2.personStatus=1 or p2.personStatus=2) " +
            "order by FIELD(g.groupState, 2, 1, 3), g.groupEnddate")
    List<Object[]> findWithGroup(@Param("memId")String memId, @Param("friendId")String friendId);

    // 친구 추가 검색(이미 친구인지)
    Optional<FriendEntity> findByMemIdAndFriendId(String memId, String friendId);
}
