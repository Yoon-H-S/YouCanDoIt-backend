package com.example.youcandoit.challenge.service.impl;

import com.example.youcandoit.challenge.repository.GodLifeChallengeRepository;
import com.example.youcandoit.challenge.service.GodLifeChallengeService;
import com.example.youcandoit.dto.GodlifeChallengeDto;
import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.entity.GodlifeChallengeEntity;
import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.friend.repository.FriendRepository;
import com.example.youcandoit.friend.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GodLifeChallengeServiceImpl implements GodLifeChallengeService {

    GodLifeChallengeRepository godLifeChallengeRepository;
    GroupRepository groupRepository;
    FriendRepository friendRepository;

    @Autowired
    public GodLifeChallengeServiceImpl(GodLifeChallengeRepository godLifeChallengeRepository, GroupRepository groupRepository, FriendRepository friendRepository) {
        this.godLifeChallengeRepository = godLifeChallengeRepository;
        this.groupRepository = groupRepository;
        this.friendRepository = friendRepository;
    }

    // 갓생 챌린지
    @Override
    public GodlifeChallengeDto godLifeChallenge(GodlifeChallengeDto gcDto) {
        Optional<GodlifeChallengeEntity> getRow = godLifeChallengeRepository.findByChallengeSubjectAndChallengeImageAndChallengeContentsAndChallengeCategory(gcDto.getChallengeSubject(), gcDto.getChallengeImage(), gcDto.getChallengeContents(), gcDto.getChallengeCategory());

        if(getRow.isEmpty()) {
            return null;
        } else {
            return getRow.get().toDto(); // Entity -> Dto로 변환
        }
    }

    // 갓생 챌린지 상세보기
    @Override
    public GodlifeChallengeDto godLifeChallengeDetail(GodlifeChallengeDto gcDto) {
        Optional<GodlifeChallengeEntity> getRow = godLifeChallengeRepository.findByChallengeSubjectAndChallengeContents(gcDto.getChallengeSubject(), gcDto.getChallengeContents());

        if(getRow.isEmpty()) {
            return null;
        } else {
            return getRow.get().toDto();
        }
    }

    // 갓생 챌린지 생성하기
    @Override
    public void saveGodlifeChallenge(GroupDto gDto) {
        GroupEntity groupEntity = gDto.toEntity();
        groupRepository.save(groupEntity);
    }

    // 갓생 챌린지 생성하기 > 함께할 친구 선택
    @Override
    public List<MemberDto> withFriend(String loginId) {
        List<MemberEntity> getRow = friendRepository.findFriendList(loginId);

        if(getRow.isEmpty()) { // 조회 결과가 없다
            return null;
        } else { // 조회 결과가 있다.
            List<MemberDto> friends = new ArrayList<MemberDto>();
            for (MemberEntity row : getRow) {
                friends.add(MemberDto.builder().memId(row.getMemId()).nickname(row.getNickname()).build());
            }
            return friends;
        }
    }



}
