package com.example.youcandoit.friend.service;

import com.example.youcandoit.dto.FriendDto;
import com.example.youcandoit.dto.MemberDto;

import java.util.List;

// 인터페이스를 생성하여 의존성을 줄인다
public interface FriendService {

    /** 친구목록의 내 프로필 */
    MemberDto myProfile(String loginId);

    /** 친구 목록 */
    List<MemberDto> friendList(String loginId);

    /** 친구 목록 검색 */
    List<MemberDto> findFriends(String loginId, String friendName);

    /** 친구 상세 프로필(같이 있는 그룹) */
    List<Object> friendProfile(FriendDto friendDto);

    /** 친구추가 페이지의 검색 */
    List<Object> searchId(FriendDto friendDto);

    /** 친구 추가 */
    void addFriend(FriendDto friendDto);
}
