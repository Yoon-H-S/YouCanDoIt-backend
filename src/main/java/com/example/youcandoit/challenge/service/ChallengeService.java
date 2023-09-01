package com.example.youcandoit.challenge.service;

import com.example.youcandoit.dto.*;

import java.sql.Date;
import java.util.List;

public interface ChallengeService {

    /** 나의 랭킹 */
    List<Object[]> myRanking(String loginId);

    /** 예약된 챌린지 */
    List<GroupDto> challengeReservation(String loginId);

    /** 챌린지 생성하기 > 함께할 친구 선택 */
    List<MemberDto> withFriend(String loginId);

    /** 갓생 챌린지 */
    List<GodlifeChallengeDto> godLifeChallenge();

    /** 갓생 챌린지 상세보기 */
    GodlifeChallengeDto godLifeChallengeDetail(String challengeSubject);

    /** 챌린지 생성하기 */
    int saveGodLifeChallenge(GroupDto groupDto, String loginId, String[] members);

    /** 챌린지 이미지 저장 */
    void saveGroupImage(GroupDto groupDto);

    /** 챌린지 랭킹 리스트 조회 */
    List<Object[]> challengeRanking(String loginId,String rankingType);

    /** 랭킹 상세 조회 */
    List<Object> rankingDetail(String rankingType, int groupNumber, Date date);

    /** diy 갤러리 리스트 조회 */
    List<Object[]> diyGallery(int groupNumber ,String memId, String loginId);

    /** diy 인증 반대 */
    int diyOpposite(OppositeDto oppositeDto);

}
