package com.example.youcandoit.friend.controller;

import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.dto.GroupPersonDto;
import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.friend.service.GroupService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/group-api")
public class GroupController {

    GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // 그룹 목록
    @GetMapping("group-list")
    public List<GroupDto> groupList(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        List<GroupDto> groupDto = groupService.groupList(loginId);

        if(groupDto == null)
            return null;
        return groupDto;
    }

    // 그룹 목록 검색
    @GetMapping("group-list/{groupName}")
    public List<GroupDto> findGroup(@PathVariable String groupName, HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        List<GroupDto> groupDto = groupService.findGroups(loginId, groupName);

        if(groupDto == null)
            return null;
        return groupDto;
    }

    // 그룹 프로필사진
    @GetMapping("group-profile-picture")
    public List<String[]> groupProfilePicture(@RequestParam int[] groupNumber) {
        List<String[]> profilePicture = groupService.groupProfilePicture(groupNumber);
        return profilePicture;
    }

    // 그룹 상세 프로필
    @GetMapping("group-profile")
    public List<Object> groupProfile(@RequestParam int groupNumber) {
        GroupDto groupDto = groupService.groupProfile(groupNumber);
        List<MemberDto> groupMember = groupService.groupMember(groupNumber);
        int[] number = {groupNumber};
        List<String[]> profilePicture = groupService.groupProfilePicture(number);

        List<Object> profile = new ArrayList<Object>();
        profile.add(groupDto);
        profile.add(groupMember);
        profile.add(profilePicture.get(0));
        return profile;
    }

    // 메인페이지 그룹 초대
    @GetMapping("main-invite")
    public List<GroupDto> mainInvite(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        List<GroupDto> groupDto = groupService.mainInvite(loginId);

        if(groupDto == null)
            return null;
        return groupDto;
    }

    // 그룹 초대 상세페이지
    @GetMapping("group-invite")
    public List<Object> groupInvite(@RequestParam int groupNumber) {
        GroupDto groupDto = groupService.groupProfile(groupNumber);
        List<Object[]> inviteMember = groupService.inviteMember(groupNumber);

        List<Object> profile = new ArrayList<Object>();
        profile.add(groupDto);
        profile.add(inviteMember);
        return profile;
    }

    // 그룹초대 수락, 거절
    @GetMapping("invite-response")
    public void inviteResponse(@RequestParam int groupNumber, @RequestParam boolean response, HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        GroupPersonDto groupPersonDto = GroupPersonDto.builder().groupNumber(groupNumber).memId(loginId).build();
        groupService.inviteResponse(groupPersonDto, response);
    }
}
