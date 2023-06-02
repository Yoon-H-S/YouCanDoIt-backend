package com.example.youcandoit.challenge.controller;

import com.example.youcandoit.challenge.service.ChallengeService;
import com.example.youcandoit.challenge.service.DailyRankingService;
import com.example.youcandoit.challenge.service.GodLifeChallengeService;
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
    GodLifeChallengeService godLifeChallengeService;
    ChallengeService challengeService;

    @Autowired
    public ChallengeController(GodLifeChallengeService godLifeChallengeService, ChallengeService challengeService) {
        this.godLifeChallengeService = godLifeChallengeService;
        this.challengeService = challengeService;
    }

    // api/challenge-api/godlife-challenge
    // 갓생 챌린지
    @PostMapping("/godlife-challenge")
    public GodlifeChallengeDto godLifeChallenge(@RequestBody GodlifeChallengeDto gcDto) {
        GodlifeChallengeDto godlifeChallengeDto = godLifeChallengeService.godLifeChallenge(gcDto);

        if (godlifeChallengeDto == null)
            return null;
        return godlifeChallengeDto;
    }

    // api/challenge-api/godlife-challenge-detail
    // 갓생 챌린지 상세보기
    @PostMapping("/godlife-challenge-detail")
    public GodlifeChallengeDto godLifeChallengeDetail(@RequestBody GodlifeChallengeDto gcDto) {
        GodlifeChallengeDto godlifeChallengeDto = godLifeChallengeService.godLifeChallengeDetail(gcDto);

        if (godlifeChallengeDto == null)
            return null;
        return godlifeChallengeDto;
    }

    // api/challenge-api/godlife-challenge-create
    // 갓생 챌린지 생성하기
    @PostMapping("/godlife-challenge-create")
    public void godLifeChallengeCreate(@RequestBody GroupDto gDto) {
        godLifeChallengeService.saveGodlifeChallenge(gDto);
    }

    // api/challenge-api/with-friend
    // 갓생 챌린지 생성하기 > 함께할 친구 선택
    @GetMapping("with-friend")
    public List<MemberDto> withFriend(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        List<MemberDto> memberDto = godLifeChallengeService.withFriend(loginId);

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
