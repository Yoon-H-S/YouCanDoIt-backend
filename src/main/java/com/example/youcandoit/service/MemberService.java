package com.example.youcandoit.service;

import com.example.youcandoit.dto.MemberDto;

// 인터페이스를 생성하여 의존성을 줄인다
public interface MemberService {
    MemberDto duplicateId(String memId);

    void saveMember(MemberDto memberDto);

    void saveProfile(MemberDto memberDto);

    MemberDto findId(MemberDto memberDto);

    MemberDto findPw(MemberDto memberDto);

    void resetPw(MemberDto memberDto);
}
