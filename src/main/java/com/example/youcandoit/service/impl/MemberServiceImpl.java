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

    @Override
    public MemberDto loginMember(MemberDto memberDto){
        /*
            1. 회원이 입력한 아이디로 DB에서 조회를 함
            2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
         */
        Optional<MemberEntity> getId = memberRepository.findById(memberDto.getMem_id());
        if(getId.isPresent()) {
            // 조회 결과가 있다(해당 아이디을 가진 회원 정보가 있다)
            MemberEntity memberEntity = getId.get();
            if (memberEntity.getPassword().equals(memberDto.getPassword())) {
                // 비밀번호 일치
                // dto -> entity 변환 후 리턴
                MemberDto dto = MemberEntity.builder().build().toDto();
                return dto;
            } else {
                // 비밀번호 불일치
                return null;
            }
        } else {
        // 조회 결과가 없다(해당 아이디를 가진 회원 정보가 없다.)
        return null;
        }
    }
}
