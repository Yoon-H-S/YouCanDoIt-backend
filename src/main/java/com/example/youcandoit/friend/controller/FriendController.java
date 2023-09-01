package com.example.youcandoit.friend.controller;

import com.example.youcandoit.dto.FriendDto;
import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.friend.service.FriendService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/friend-api")
public class FriendController {

    FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    /** 내 프로필 */
    @GetMapping("my-profile")
    public MemberDto myProfile(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        MemberDto memberDto = friendService.myProfile(loginId);
        return memberDto;
    }

     /** 친구 목록 */
    @GetMapping("friend-list")
    public List<MemberDto> friendList(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        List<MemberDto> memberDto = friendService.friendList(loginId);

        if(memberDto == null)
            return null;
        return memberDto;
    }

    /** 친구 목록 검색 */
    @GetMapping("friend-list/{friendName}")
    public List<MemberDto> findFriend(@PathVariable String friendName, HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        List<MemberDto> memberDto = friendService.findFriends(loginId, friendName);

        if(memberDto == null)
            return null;
        return memberDto;
    }

    /** 친구 상세 프로필(같이 속한 그룹) */
    @PostMapping("friend-profile")
    public List<Object> friendProfile(@RequestBody FriendDto friendDto, HttpSession session) {
        friendDto.setMemId((String)session.getAttribute("loginId"));
        List<Object> friendProfile = friendService.friendProfile(friendDto);

        return friendProfile;
    }

    /**  친구 추가 페이지의 내 아이디 */
    @GetMapping("my-id")
    public String myId(HttpSession session) {
        return (String)session.getAttribute("loginId");
    }

    /** 친구 추가 페이지 검색 */
    @PostMapping("/search-id")
    public List<Object> searchId(@RequestBody FriendDto friendDto, HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");

        if(loginId.equals(friendDto.getFriendId())) { // 입력한 아이디가 자기 자신이라면
            MemberDto memberDto = friendService.myProfile(loginId);

            List<Object> response = new ArrayList<Object>();
            response.add("3"); // 자기 자신인 상태
            response.add(memberDto);

            return response;
        } else {
            friendDto.setMemId(loginId);
            List<Object> response = friendService.searchId(friendDto);

            if(response == null)
                return null;
            return response;
        }
    }

    /** 친구 추가 */
    @PostMapping("add-friend")
    public void addFriend(@RequestBody FriendDto friendDto, HttpSession session) {
        friendDto.setMemId((String)session.getAttribute("loginId"));
        friendService.addFriend(friendDto);
    }
}
