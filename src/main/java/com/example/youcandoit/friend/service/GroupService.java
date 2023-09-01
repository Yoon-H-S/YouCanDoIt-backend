package com.example.youcandoit.friend.service;

import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.dto.GroupPersonDto;
import com.example.youcandoit.dto.MemberDto;

import java.util.List;

// 인터페이스를 생성하여 의존성을 줄인다
public interface GroupService {

    /** 그룹 목록 */
    List<GroupDto> groupList(String loginId);

    /** 그룹 목록 검색 */
    List<GroupDto> findGroups(String loginId, String groupName);

    /** 그룹 프로필사진 */
    List<String[]> groupProfilePicture(int[] groupNumber);

    /** 그룹 멤버 */
    List<MemberDto> groupMember(int groupNumber);

    /** 그룹 상세 프로필 */
    GroupDto groupProfile(int groupNumber);

    /** 메인페이지 그룹 초대 */
    List<GroupDto> mainInvite(String loginId);

    /** 그룹에 초대된 멤버들 */
    List<Object[]> inviteMember(int groupNumber);

    /** 그룹초대 수락, 거절 */
    void inviteResponse(GroupPersonDto groupPersonDto, boolean response);
}
