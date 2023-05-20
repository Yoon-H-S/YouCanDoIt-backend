package com.example.youcandoit.friend.service;

import com.example.youcandoit.friend.dto.FriendDto;
import com.example.youcandoit.member.dto.MemberDto;

// 인터페이스를 생성하여 의존성을 줄인다
public interface FriendService {
    void plusFriend(FriendDto friendDto);

    FriendDto getId(String friendId);

    FriendDto listfriend(FriendDto friendDto);

}
