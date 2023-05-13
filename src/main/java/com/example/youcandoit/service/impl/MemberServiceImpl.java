package com.example.youcandoit.service.impl;

import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.repository.MemberRepository;
import com.example.youcandoit.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    MemberRepository memberRepository;

    @Autowired // 자동 연결
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void saveMember(MemberDto memberDto) {
        String join_date = String.valueOf(LocalDate.now());
        // Dto -> Entity로 변환
        MemberEntity memberEntity = memberDto.toEntity(join_date);
        // db에 insert
        memberRepository.save(memberEntity);
    }

    @Override
    public MemberDto duplicateId(String mem_id) {
        // db에서 값받아서 Entity에 저장
        Optional<MemberEntity> getId = memberRepository.findById(mem_id);

        if(getId.isEmpty())
            return null;
        else
            return getId.get().toDto(); // Entity -> Dto로 변환
    }
}
