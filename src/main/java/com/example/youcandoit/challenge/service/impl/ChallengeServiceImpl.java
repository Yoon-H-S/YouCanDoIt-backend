package com.example.youcandoit.challenge.service.impl;

import com.example.youcandoit.entity.GroupPersonEntity;
import com.example.youcandoit.repository.GodLifeChallengeRepository;
import com.example.youcandoit.challenge.service.ChallengeService;
import com.example.youcandoit.dto.GodlifeChallengeDto;
import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.entity.GodlifeChallengeEntity;
import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.repository.FriendRepository;
import com.example.youcandoit.repository.GroupPersonRepository;
import com.example.youcandoit.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChallengeServiceImpl implements ChallengeService {
    GroupRepository groupRepository;
    GroupPersonRepository groupPersonRepository;
    GodLifeChallengeRepository godLifeChallengeRepository;
    FriendRepository friendRepository;

    @Autowired
    public ChallengeServiceImpl(
            GodLifeChallengeRepository godLifeChallengeRepository,
            GroupRepository groupRepository,
            FriendRepository friendRepository,
            GroupPersonRepository groupPersonRepository) {
        this.groupPersonRepository = groupPersonRepository;
        this.godLifeChallengeRepository = godLifeChallengeRepository;
        this.groupRepository = groupRepository;
        this.friendRepository = friendRepository;
    }


    // 나의 랭킹
    @Override
    public List<Object[]> myRanking(String loginId) {
        Date date = Date.valueOf(LocalDate.now());
        List<Object[]> getRow = groupPersonRepository.findGroup(loginId, date);
        List<Object[]> myRankData = new ArrayList<Object[]>();
        for(Object[] row : getRow) {
            GroupDto groupDto = ((GroupEntity)row[0]).toDto();
            long dDay = (groupDto.getGroupEnddate().getTime() - date.getTime()) / (1000 * 60 * 60 * 24); // getTime()의 반환값이 밀리세컨드이기 때문에 계산을 해야 한다.
            // 랭킹, 남은날짜, 그룹이미지, 그룹주제, 그룹이름
            myRankData.add(new Object[]{groupDto.getGroupName(), groupDto.getGroupSubject(), groupDto.getGroupImage(), dDay, row[1]});
        }
        return myRankData;
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

    // 갓생 챌린지
    @Override
    public List<GodlifeChallengeDto> godLifeChallenge() {
        List<GodlifeChallengeEntity> getRow = godLifeChallengeRepository.findAll();

        List<GodlifeChallengeDto> godlifeList = new ArrayList<GodlifeChallengeDto>();
        for(GodlifeChallengeEntity row : getRow) {
            godlifeList.add(row.toDto());
        }

        return godlifeList;
    }

    // 갓생 챌린지 상세보기
    @Override
    public GodlifeChallengeDto godLifeChallengeDetail(String challengeSubject) {
        Optional<GodlifeChallengeEntity> getRow = godLifeChallengeRepository.findById(challengeSubject);

        return getRow.get().toDto();
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

    // 갓생 챌린지 생성하기
    @Override
    public int saveGodlifeChallenge(GroupDto groupDto) {
        GroupEntity groupEntity = groupDto.toEntity();
        int groupNumber = groupRepository.save(groupEntity).getGroupNumber();

        return groupNumber;
    }

    // 그룹 인원 생성하기
    @Override
    public void saveGroupPerson(int groupMember, String loginId, String[] members) {
        GroupPersonEntity groupPersonEntity = GroupPersonEntity.builder().groupNumber(groupMember).build();
        for(String member : members) {
            groupPersonEntity.setMemId(member);
            groupPersonRepository.save(groupPersonEntity);
        }

        groupPersonEntity.setMemId(loginId);
        groupPersonEntity.setPersonStatus("1");
        groupPersonRepository.save(groupPersonEntity);
    }
}
