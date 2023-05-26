package com.example.youcandoit.friend.repository;


import com.example.youcandoit.entity.FriendEntity;
import com.example.youcandoit.entity.Id.FriendId;
import com.example.youcandoit.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<FriendEntity, FriendId> { // <>안에는 Entity, Primarykey를 넣어준다.
    @Query(value = "select m from FriendEntity f join MemberEntity m on f.friendId = m.memId where f.memId=:loginId")
    List<MemberEntity> findFriendList(@Param("loginId")String loginId);
    @Query(value = "select m from FriendEntity f join MemberEntity m on f.friendId = m.memId where f.memId=:loginId and m.nickname like %:friendName%")
    List<MemberEntity> findMyFriends(@Param("loginId")String loginId, @Param("friendName")String friendName);
}
