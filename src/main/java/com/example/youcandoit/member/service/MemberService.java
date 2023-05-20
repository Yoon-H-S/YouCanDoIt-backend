package com.example.youcandoit.member.service;

import com.example.youcandoit.member.dto.MemberDto;

// 인터페이스를 생성하여 의존성을 줄인다
public interface MemberService {
    MemberDto loginMember(MemberDto memberDto);
    MemberDto getId(String memId);

    void saveMember(MemberDto memberDto);

    void saveProfile(MemberDto memberDto);

    MemberDto findId(MemberDto memberDto);

    MemberDto findPw(MemberDto memberDto);

    void resetPw(MemberDto memberDto);
}
