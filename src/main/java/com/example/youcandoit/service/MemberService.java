package com.example.youcandoit.service;

import com.example.youcandoit.dto.MemberDto;

// 인터페이스를 생성하여 의존성을 줄인다
public interface MemberService {
    void saveMember(MemberDto memberDto);

    MemberDto duplicateId(String mem_id);
}
