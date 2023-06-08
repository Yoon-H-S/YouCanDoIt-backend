package com.example.youcandoit.challenge.controller;

import com.example.youcandoit.challenge.service.ChallengeService;
import com.example.youcandoit.dto.GodlifeChallengeDto;
import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.dto.MemberDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
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

    // 갓생 챌린지 일일랭킹 목록
    @GetMapping("daily-ranking")
    public List<Object[]> DailyRanking(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        return challengeService.dailyRanking(loginId);
    }

    // 갓생 챌린지 일일랭킹 상세
    @GetMapping("daily-ranking-detail")
    public List<Object> dailyRankingDetail(int groupNumber, Date date) {
        return challengeService.dailyRankingDetail(groupNumber, date);
    }

    // 갓생 챌린지 누적랭킹 목록
    @GetMapping("godlife-ranking")
    public List<Object[]> godlifeRanking(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        return challengeService.accumulateRanking(loginId);
    }

    // 갓생 챌린지 누적랭킹 상세
    @GetMapping("godlife-ranking-detail")
    public List<Object> godlifeRankingDetail(int groupNumber) {
        return challengeService.accumulateRankingDetail(groupNumber);
    }

    // api/challenge-api/godlife-challenge-list
    // 갓생 챌린지 목록
    @GetMapping("/godlife-challenge-list")
    public List<GodlifeChallengeDto> godLifeChallenge() {
        return challengeService.godLifeChallenge();
    }

    // api/challenge-api/godlife-challenge-detail
    // 갓생 챌린지 상세보기
    @GetMapping("/godlife-challenge-detail")
    public GodlifeChallengeDto godLifeChallengeDetail(@RequestParam String subject) {
        return challengeService.godLifeChallengeDetail(subject);
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

    // api/challenge-api/challenge-create
    // 갓생 챌린지 생성하기
    @PostMapping("/challenge-create")
    public int godLifeChallengeCreate(@RequestBody GroupDto groupDto, @RequestParam String[] members, HttpSession session) {
        String defaultGroupImage = "/groupImage/defaultGroup.png";
        groupDto.setGroupImage(defaultGroupImage);
        String loginId = (String)session.getAttribute("loginId");
        int groupNumber = challengeService.saveGodlifeChallenge(groupDto, loginId, members);
        return groupNumber;
    }

    // 그룹 이미지
    @PostMapping("/insert-group-image")
    public void insertGroupImage(@RequestParam int groupNumber, @RequestPart MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.length()-4); // 확장자 추출
        String dbName = "/groupImage/"+ "Image" + groupNumber  + extension; //  db에 저장될 경로, 저장명 지정
        String saveName = "/home/yun/ycdi/build/groupImage/" + "Image" + groupNumber + extension; // 실제 저장경로, 저장명 지정

        try {
            file.transferTo(new File(saveName)); // 파일 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
        GroupDto groupDto = GroupDto.builder()
                .groupNumber(groupNumber)
                .groupImage(dbName)
                .build();
        challengeService.saveGroupImage(groupDto);
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

}
