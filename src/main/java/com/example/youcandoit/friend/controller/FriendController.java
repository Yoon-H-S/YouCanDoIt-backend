package com.example.youcandoit.friend.controller;

import com.example.youcandoit.friend.dto.FriendDto;
import com.example.youcandoit.friend.service.FriendService;
import com.example.youcandoit.member.dto.MemberDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/friend-api")
public class FriendController {

    FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
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

    // api/friend-api/list-friend
    // 친구 목록
    @PostMapping("list-friend")
    public String listFriend(@RequestBody FriendDto fDto, HttpSession session) {
        FriendDto friendDto = friendService.listfriend(fDto);

        if(friendDto == null)
            return null;
        session.setAttribute("nickname", friendDto.getFriendId());
        return friendDto.getFriendId();
    }
}
