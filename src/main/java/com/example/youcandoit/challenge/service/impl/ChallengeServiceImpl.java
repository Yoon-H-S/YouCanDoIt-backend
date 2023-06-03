package com.example.youcandoit.challenge.service.impl;

import com.example.youcandoit.challenge.service.ChallengeService;
import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.friend.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChallengeServiceImpl implements ChallengeService {
    GroupRepository groupRepository;

    @Autowired
    public ChallengeServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    // 예약된 챌린지
    @Override
    public List<GroupDto> challengeReservation(String loginId) {
        List<GroupEntity> getRow = groupRepository.findChallengeReservation(loginId);

        List<GroupDto> groups = new ArrayList<GroupDto>();
        for(GroupEntity row : getRow) {
            groups.add(GroupDto.builder()
                    .groupState(row.getGroupState())
                    .groupSubject(row.getGroupSubject())
                    .groupName(row.getGroupName())
                    .groupStartdate(row.getGroupStartdate())
                    .groupEnddate(row.getGroupEnddate())
                    .groupImage(row.getGroupImage())
                    .build());
        }
        return groups;
    }
}
