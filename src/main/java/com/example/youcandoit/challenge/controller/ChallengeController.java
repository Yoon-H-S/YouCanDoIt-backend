package com.example.youcandoit.challenge.controller;

import com.example.youcandoit.challenge.service.ChallengeService;
import com.example.youcandoit.dto.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


    /** 메인페이지 나의랭킹 */
    @GetMapping("/my-ranking")
    public List<Object[]> myRanking(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        List<Object[]> myRank = challengeService.myRanking(loginId);
        return myRank;
    }

    /** 챌린지 랭킹 목록 */
    @GetMapping("/challenge-ranking/{rankingType}")
    public List<Object[]> challengeRanking(HttpSession session, @PathVariable String rankingType) {
        String loginId = (String)session.getAttribute("loginId");
        return challengeService.challengeRanking(loginId, rankingType);
    }

    /** 랭킹 상세 */
    @GetMapping("/ranking-detail/{rankingType}")
    public List<Object> rankingDetail(@PathVariable String rankingType, @RequestParam int groupNumber, @RequestParam(required = false) Date date) {
        return challengeService.rankingDetail(rankingType ,groupNumber, date);
    }

    /** diy 챌린지 갤러리 */
    @GetMapping("/diy-gallery")
    public List<Object[]> diyGallery(@RequestParam int groupNumber, @RequestParam(required = false) String memId, HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        return challengeService.diyGallery(groupNumber, memId, loginId);
    }

    /** diy 인증 반대 */
    @PostMapping("/diy-opposite")
    public int diyOpposite(@RequestBody OppositeDto oppositeDto, HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        oppositeDto.setOppositeId(loginId);
        return challengeService.diyOpposite(oppositeDto);
    }

    /** 갓생 챌린지 목록 */
    @GetMapping("/godlife-challenge-list")
    public List<GodlifeChallengeDto> godLifeChallenge() {
        return challengeService.godLifeChallenge();
    }

    /** 갓생 챌린지 상세보기 */
    @GetMapping("/godlife-challenge-detail")
    public GodlifeChallengeDto godLifeChallengeDetail(@RequestParam String subject) {
        return challengeService.godLifeChallengeDetail(subject);
    }

    /** 챌린지 생성하기 > 함께할 친구 선택 */
    @GetMapping("/with-friend")
    public List<MemberDto> withFriend(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        List<MemberDto> memberDto = challengeService.withFriend(loginId);

        if (memberDto == null)
            return null;
        return memberDto;
    }

    /** 챌린지 생성하기 */
    @PostMapping("/challenge-create")
    public int challengeCreate(@RequestBody GroupDto groupDto, @RequestParam String[] members, HttpSession session) {
        String defaultGroupImage = "/groupImage/defaultGroup.png";
        groupDto.setGroupImage(defaultGroupImage);
        String loginId = (String)session.getAttribute("loginId");
        int groupNumber = challengeService.saveGodLifeChallenge(groupDto, loginId, members);
        return groupNumber;
    }

    /** 그룹 이미지 */
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

    /** 예약된 챌린지 */
    @PostMapping("/challenge-reservation")
    public List<GroupDto> challengeReservation(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        List<GroupDto> groupDto = challengeService.challengeReservation(loginId);

        if (groupDto == null)
            return null;
        return groupDto;
    }
}
