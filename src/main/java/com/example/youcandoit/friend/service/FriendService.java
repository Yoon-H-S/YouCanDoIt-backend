package com.example.youcandoit.friend.service;

import com.example.youcandoit.dto.FriendDto;
import com.example.youcandoit.dto.MemberDto;

import java.util.List;

// 인터페이스를 생성하여 의존성을 줄인다
public interface FriendService {
    void plusFriend(FriendDto friendDto);
    FriendDto getId(String friendId);
    List<MemberDto> friendList(String loginId);
    List<MemberDto> findFriends(String loginId, String friendName);

}
