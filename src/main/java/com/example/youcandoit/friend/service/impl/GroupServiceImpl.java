package com.example.youcandoit.friend.service.impl;

import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.friend.repository.GroupRepository;
import com.example.youcandoit.friend.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {
    GroupRepository groupRepository;

    @Autowired // 자동 연결
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
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
                friends.add(GroupDto.builder().groupName(row.getGroupName()).groupNumber(row.getGroupNumber()).build());
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
                friends.add(GroupDto.builder().groupName(row.getGroupName()).groupNumber(row.getGroupNumber()).build());
            }
            return friends;
        }
    }

    // 그룹 프로필사진
    @Override
    public List<MemberDto> groupMember(int groupNumber) {
        List<MemberEntity> getRow = groupRepository.findGroupMember(groupNumber);

        if(getRow.isEmpty()) {
            return null;
        } else {
            List<MemberDto> members = new ArrayList<MemberDto>();
            for(MemberEntity row : getRow) {
                members.add(MemberDto.builder().memId(row.getMemId()).profilePicture(row.getProfilePicture()).nickname(row.getNickname()).build());
            }
            return members;
        }
    }

    // 그룹 상세 프로필
    @Override
    public GroupDto groupProfile(int groupNumber) {
        Optional<GroupEntity> getRow = groupRepository.findById(groupNumber);
        return getRow.get().toDto();
    }
}


