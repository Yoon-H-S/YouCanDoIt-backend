package com.example.youcandoit.friend.service.impl;

import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.dto.GroupPersonDto;
import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.entity.GroupPersonEntity;
import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.repository.GroupPersonRepository;
import com.example.youcandoit.repository.GroupRepository;
import com.example.youcandoit.friend.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {
    GroupRepository groupRepository;
    GroupPersonRepository groupPersonRepository;

    @Autowired // 자동 연결
    public GroupServiceImpl(GroupRepository groupRepository, GroupPersonRepository groupPersonRepository) {
        this.groupRepository = groupRepository;
        this.groupPersonRepository = groupPersonRepository;
    }

    // 그룹 목록
    @Override
    public List<GroupDto> groupList(String loginId) {
        List<GroupEntity> getRow = groupRepository.findGroupList(loginId);

        if(getRow.isEmpty()) { // 조회 결과가 없다
            return null;
        } else { // 조회 결과가 있다.
            List<GroupDto> friends = new ArrayList<GroupDto>();
            for(GroupEntity row : getRow) {
                friends.add(GroupDto.builder().groupName(row.getGroupName()).groupNumber(row.getGroupNumber()).groupHeadcount(row.getGroupHeadcount()).build());
            }
            return friends;
        }
    }

    // 그룹 목록 검색
    @Override
    public List<GroupDto> findGroups(String loginId, String groupName) {
        List<GroupEntity> getRow = groupRepository.findSearchGroups(loginId, groupName);

        if(getRow.isEmpty()) { // 조회 결과가 없다
            return null;
        } else { // 조회 결과가 있다.
            List<GroupDto> friends = new ArrayList<GroupDto>();
            for(GroupEntity row : getRow) {
                friends.add(GroupDto.builder().groupName(row.getGroupName()).groupNumber(row.getGroupNumber()).groupHeadcount(row.getGroupHeadcount()).build());
            }
            return friends;
        }
    }

    // 그룹 프로필사진
    @Override
    public List<String[]> groupProfilePicture(int[] groupNumber) {
        List<String[]> profilePicture = new ArrayList<String[]>();
        for(int number : groupNumber) {
            List<String> getRow = groupRepository.findProfilePicture(number);

            profilePicture.add(getRow.toArray(new String[getRow.size()]));
        }
        return profilePicture;
    }

    // 그룹 멤버
    @Override
    public List<MemberDto> groupMember(int groupNumber) {
        List<MemberEntity> getRow = groupRepository.findGroupMember(groupNumber);

        List<MemberDto> members = new ArrayList<MemberDto>();
        for(MemberEntity row : getRow) {
            members.add(MemberDto.builder().memId(row.getMemId()).profilePicture(row.getProfilePicture()).nickname(row.getNickname()).build());
        }
        return members;
    }

    // 그룹 상세 프로필
    @Override
    public GroupDto groupProfile(int groupNumber) {
        Optional<GroupEntity> getRow = groupRepository.findById(groupNumber);
        return getRow.get().toDto();
    }

    // 메인페이지 그룹 초대
    @Override
    public List<GroupDto> mainInvite(String loginId) {
        List<GroupEntity> getRow = groupPersonRepository.findGroupInvite(loginId);

        List<GroupDto> groups = new ArrayList<GroupDto>();
        for(GroupEntity row : getRow) {
            groups.add(GroupDto.builder()
                    .groupNumber(row.getGroupNumber())
                    .groupName(row.getGroupName())
                    .groupSubject(row.getGroupSubject())
                    .groupImage(row.getGroupImage())
                    .build());
        }
        return groups;
    }

    // 그룹에 초대된 멤버들
    @Override
    public List<Object[]> inviteMember(int groupNumber) {
        List<Object[]> getRow = groupPersonRepository.findInviteMember(groupNumber);
        return getRow;
    }

    // 그룹초대 수락, 거절
    @Override
    public void inviteResponse(GroupPersonDto groupPersonDto, boolean response) {
        GroupPersonEntity groupPersonEntity = groupPersonDto.toEntity();
        if(response) {
            groupPersonEntity.setPersonStatus("2");
            groupPersonRepository.save(groupPersonEntity);
            groupRepository.updateGroupMember(groupPersonEntity.getGroupNumber());
        } else {
            groupPersonRepository.delete(groupPersonEntity);
        }
    }
}


