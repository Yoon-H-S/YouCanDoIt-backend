package com.example.youcandoit.challenge.service;

import com.example.youcandoit.dto.GodlifeChallengeDto;
import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.dto.MemberDto;

import java.sql.Date;
import java.util.List;

public interface ChallengeService {
    List<Object[]> myRanking(String loginId);
    List<GroupDto> challengeReservation(String loginId);
    List<MemberDto> withFriend(String loginId);

    List<GodlifeChallengeDto> godLifeChallenge();

    GodlifeChallengeDto godLifeChallengeDetail(String challengeSubject);

    int saveGodlifeChallenge(GroupDto groupDto);
    void saveGroupPerson(int groupMember, String loginId, String[] members);
    List<Object[]> dailyRanking(String loginId);
    List<Object> dailyRankingDetail(int groupNumber, Date date);
    List<Object[]> accumulateRanking(String loginId);
    List<Object> accumulateRankingDetail(int groupNumber);
}
