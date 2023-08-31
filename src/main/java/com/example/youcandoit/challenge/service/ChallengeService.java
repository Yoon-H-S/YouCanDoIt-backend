package com.example.youcandoit.challenge.service;

import com.example.youcandoit.dto.*;

import java.sql.Date;
import java.util.List;

public interface ChallengeService {
    List<Object[]> myRanking(String loginId);
    List<GroupDto> challengeReservation(String loginId);
    List<MemberDto> withFriend(String loginId);
    List<GodlifeChallengeDto> godLifeChallenge();
    GodlifeChallengeDto godLifeChallengeDetail(String challengeSubject);
    int saveGodLifeChallenge(GroupDto groupDto, String loginId, String[] members);
    void saveGroupImage(GroupDto groupDto);
    List<Object[]> challengeRanking(String loginId,String rankingType);
    List<Object> rankingDetail(String rankingType, int groupNumber, Date date);
    List<Object[]> diyGallery(int groupNumber ,String memId, String loginId);
    int diyOpposite(OppositeDto oppositeDto);

    void methodsTest1();
}
