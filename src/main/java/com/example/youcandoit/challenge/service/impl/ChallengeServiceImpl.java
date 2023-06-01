package com.example.youcandoit.challenge.service.impl;

import com.example.youcandoit.challenge.service.ChallengeService;
import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.friend.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public GroupDto challengeReservation(GroupDto gDto) {
        Optional<GroupEntity> getRow = groupRepository.findByGroupSubjectAndGroupNameAndGroupStartdateAndGroupEnddateAndGroupImage(gDto.getGroupSubject(), gDto.getGroupName(), gDto.getGroupStartdate(), gDto.getGroupEnddate(), gDto.getGroupImage());

        if (getRow.isEmpty())  {
            return null;
        } else {
            return getRow.get().toDto();
        }
    }


}
