package com.example.youcandoit.controller;

import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("api/member-api")
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // api/member-api/duplicate-id/{id}
    // 아이디 중복 확인
    @GetMapping("/duplicate-id/{memId}")
    public boolean duplicate(@PathVariable String memId) {
        MemberDto memberDto = memberService.duplicateId(memId);

        if(memberDto == null)
            return false;
        else
            return true;
    }

    // api/member-api/signup
    // 회원가입
    @PostMapping("/signup")
    public void createMember(@RequestBody MemberDto memberDto) {
        memberDto.setJoinDate(String.valueOf(LocalDate.now()));
        memberService.saveMember(memberDto);
    }

    // api/member-api/insert-profile
    // 프로필사진 업로드
    @PostMapping("/insert-profile")
    public void insertProfile(@RequestParam String memId, @RequestPart MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.length()-4); // 확장자 추출
        String path = "D:/project/profile/"; // 저장 경로 지정
        String safeName = path + memId + "Profile" + extension; // 저장명 지정

        try {
            file.transferTo(new File(safeName)); // 파일 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
        MemberDto memberDto = MemberDto.builder()
                .memId(memId)
                .profilePicture(safeName)
                .build();
        memberService.saveProfile(memberDto);
    }

    // 전화번호로 아이디 찾기
    @PostMapping("/find-id")
    public MemberDto findId(@RequestBody MemberDto mDto) {
        MemberDto memberDto = memberService.findId(mDto);

        if(memberDto == null)
            return null;
        else
            return memberDto;
    }

    // 아이디와 전화번호가 일치하는 회원이 있는지 확인
    @PostMapping("/find-pw")
    public MemberDto findPw(@RequestBody MemberDto mDto) {
        MemberDto memberDto = memberService.findPw(mDto);

        if(memberDto == null)
            return null;
        else
            return memberDto;
    }

    // 비밀번호 재설정
    @PostMapping("/pw-reset")
    public void pwReset(@RequestBody MemberDto memberDto) {
        memberService.resetPw(memberDto);
    }
}
