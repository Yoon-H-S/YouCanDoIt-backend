package com.example.youcandoit.member.service;

import com.example.youcandoit.dto.MemberDto;

// 인터페이스를 생성하여 의존성을 줄인다
public interface MemberService {

    /** 로그인 */
    MemberDto loginMember(MemberDto memberDto);

    /** 중복아이디 확인(회원가입) */
    MemberDto getId(String memId);

    /** 회원가입 */
    void saveMember(MemberDto memberDto);

    /** 프로필사진경로 저장 */
    void saveProfile(MemberDto memberDto);

    /** 전화번호로 아이디 찾기 */
    MemberDto findId(MemberDto memberDto);

    /** 비밀번호 찾기(아이디 & 전화번호) */
    MemberDto findPw(MemberDto memberDto);

    /** 비밀번호 재설정 */
    void resetPw(MemberDto memberDto);
}
