package com.example.youcandoit.friend.service;

import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.dto.GroupPersonDto;
import com.example.youcandoit.dto.MemberDto;

import java.util.List;

// 인터페이스를 생성하여 의존성을 줄인다
public interface GroupService {
    List<GroupDto> groupList(String loginId);
    List<GroupDto> findGroups(String loginId, String groupName);
    List<String[]> groupProfilePicture(int[] groupNumber);
    List<MemberDto> groupMember(int groupNumber);
    GroupDto groupProfile(int groupNumber);
    List<GroupDto> mainInvite(String loginId);
    List<Object[]> inviteMember(int groupNumber);
    void inviteResponse(GroupPersonDto groupPersonDto, boolean response);
}
