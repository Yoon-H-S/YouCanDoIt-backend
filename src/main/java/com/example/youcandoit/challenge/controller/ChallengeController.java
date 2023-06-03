package com.example.youcandoit.challenge.controller;

import com.example.youcandoit.challenge.service.ChallengeService;
import com.example.youcandoit.dto.GodlifeChallengeDto;
import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.dto.MemberDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

@RestController
@RequestMapping("api/challenge-api")
public class ChallengeController {
    ChallengeService challengeService;

    @Autowired
    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }


    // 메인페이지 나의랭킹
    @GetMapping("my-ranking")
    public List<Object[]> myRanking(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        List<Object[]> myRank = challengeService.myRanking(loginId);
        return myRank;
    }

    // api/challenge-api/godlife-challenge
    // 갓생 챌린지
    @PostMapping("/godlife-challenge")
    public GodlifeChallengeDto godLifeChallenge(@RequestBody GodlifeChallengeDto gcDto) {
        GodlifeChallengeDto godlifeChallengeDto = challengeService.godLifeChallenge(gcDto);

        if (godlifeChallengeDto == null)
            return null;
        return godlifeChallengeDto;
    }

    // api/challenge-api/godlife-challenge-detail
    // 갓생 챌린지 상세보기
    @PostMapping("/godlife-challenge-detail")
    public GodlifeChallengeDto godLifeChallengeDetail(@RequestBody GodlifeChallengeDto gcDto) {
        GodlifeChallengeDto godlifeChallengeDto = challengeService.godLifeChallengeDetail(gcDto);

        if (godlifeChallengeDto == null)
            return null;
        return godlifeChallengeDto;
    }

    // api/challenge-api/godlife-challenge-create
    // 갓생 챌린지 생성하기
    @PostMapping("/godlife-challenge-create")
    public void godLifeChallengeCreate(@RequestBody GroupDto gDto) {
        challengeService.saveGodlifeChallenge(gDto);
    }

    // api/challenge-api/with-friend
    // 갓생 챌린지 생성하기 > 함께할 친구 선택
    @GetMapping("with-friend")
    public List<MemberDto> withFriend(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        List<MemberDto> memberDto = challengeService.withFriend(loginId);

        if (memberDto == null)
            return null;
        return memberDto;
    }

    // api/challenge-api/challenge-reservation
    // 예약된 챌린지
    @PostMapping("challenge-reservation")
    public List<GroupDto> challengeReservation(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        List<GroupDto> groupDto = challengeService.challengeReservation(loginId);

        if (groupDto == null)
            return null;
        return groupDto;
    }

//    // api/challenge-api/daily-ranking
//    // 갓생 챌린지 > 일일 랭킹
//    @PostMapping("daily-ranking")
//    public

}
