package com.example.youcandoit.controller;

import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/duplicate-id/{mem_id}")
    public boolean duplicate(@PathVariable String mem_id) {
        MemberDto memberDto = memberService.duplicateId(mem_id);

        if(memberDto == null)
            return false;
        else
            return true;
    }

    // api/member-api/signup
    // 회원가입
    @PostMapping("/signup")
    public void createMember(@RequestBody MemberDto memberDto) {
        memberService.saveMember(memberDto);
    }

    // kim test
}
