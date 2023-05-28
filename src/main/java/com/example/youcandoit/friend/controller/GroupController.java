package com.example.youcandoit.friend.controller;

import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.friend.service.GroupService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    // 그룹 멤버(그룹 프로필),(그룹 프로필사진)
    @GetMapping("group-member")
    public List<MemberDto> groupProfilePicture(@RequestParam int groupNumber) {
        List<MemberDto> groupMember = groupService.groupMember(groupNumber);
        return groupMember;
    }

    // 그룹 상세 프로필
    @GetMapping("group-profile")
    public GroupDto groupProfile(@RequestParam int groupNumber) {
        GroupDto groupDto = groupService.groupProfile(groupNumber);
        return groupDto;
    }
}
