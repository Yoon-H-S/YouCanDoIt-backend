package com.example.youcandoit.friend.controller;

import com.example.youcandoit.dto.FriendDto;
import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.friend.service.FriendService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/friend-api")
public class FriendController {

    FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }



    // api/friend-api/friend-list
    // 친구 목록
    @GetMapping("friend-list")
    public List<MemberDto> listFriend(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        List<MemberDto> memberDto = friendService.friendList(loginId);

        if(memberDto == null)
            return null;
        return memberDto;
    }

    // api/friend-api/friend-list/
    // 친구 목록 검색
    @GetMapping("friend-list/{friendName}")
    public List<MemberDto> findFriend(@PathVariable String friendName, HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        List<MemberDto> memberDto = friendService.findFriends(loginId, friendName);

        if(memberDto == null)
            return null;
        return memberDto;
    }

    // api/friend-api/add-friend
    // 친구 추가
    @PostMapping("add-friend")
    public void addFriend(@RequestBody FriendDto friendDto) {
        friendService.plusFriend(friendDto);
    }

    // api/friend-api/duplicate-id/{id}
    // 친구추가 중복 확인
    @GetMapping("/duplicate-id/{friendId}")
    public boolean duplicate(@PathVariable String friendId) {
        FriendDto friendDto = friendService.getId(friendId);

        if(friendDto == null)
            return false;
        return true;
    }


}
