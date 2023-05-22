package com.example.youcandoit.member.service.impl;

import com.example.youcandoit.member.dto.MemberDto;
import com.example.youcandoit.member.entity.MemberEntity;
import com.example.youcandoit.member.service.MemberService;
import com.example.youcandoit.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    MemberRepository memberRepository;

    @Autowired // 자동 연결
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberDto loginMember(MemberDto memberDto){
        // 회원이 입력한 아이디와 비밀번호로 DB에서 조회
        Optional<MemberEntity> getColumn = memberRepository.findByPasswordAndMemIdAndMemClass(memberDto.getPassword() ,memberDto.getMemId(), memberDto.getMemClass());

        if(getColumn.isPresent()) { // 조회 결과가 있다(해당 아이디을 가진 회원 정보가 있다)
            return getColumn.get().toDto();
        } else { // 조회 결과가 없다(해당 아이디를 가진 회원 정보가 없다.
            return null;
        }
    }

    // 아이디로 컬럼 조회(중복 아이디 조회, 세션값으로 닉네임 받기)
    @Override
    public MemberDto getId(String memId) {
        // db에서 값받아서 Entity에 저장
        Optional<MemberEntity> getColumn = memberRepository.findById(memId);

        if(getColumn.isEmpty())
            return null;
        else
            return getColumn.get().toDto(); // Entity -> Dto로 변환
    }

    // 회원가입
    @Override
    public void saveMember(MemberDto memberDto) {
        // Dto -> Entity로 변환
        MemberEntity memberEntity = memberDto.toEntity();
        // db에 insert
        memberRepository.save(memberEntity);
    }

    // 프로필사진경로 저장
    @Override
    public void saveProfile(MemberDto memberDto) {
        Optional<MemberEntity> getColumn = memberRepository.findById(memberDto.getMemId());
        MemberEntity memberEntity = getColumn.get();
        memberEntity.setProfilePicture(memberDto.getProfilePicture());

        memberRepository.save(memberEntity);
    }

    // 전화번호로 아이디 찾기
    @Override
    public MemberDto findId(MemberDto memberDto) {
        Optional<MemberEntity> getColumn = memberRepository.findByPhoneNumber(memberDto.getPhoneNumber());

        if(getColumn.isEmpty()) {
            return null;
        } else {
            return getColumn.get().toDto(); // Entity -> Dto로 변환
        }
    }

    // 아이디와 전화번호가 일치하는 회원이 있는지 확인
    @Override
    public MemberDto findPw(MemberDto memberDto) {
        Optional<MemberEntity> getColumn = memberRepository.findByPhoneNumberAndMemId(memberDto.getPhoneNumber(), memberDto.getMemId());

        if(getColumn.isEmpty()) {
            return null;
        } else {
            return getColumn.get().toDto(); // Entity -> Dto로 변환
        }
    }

    // 비밀번호 재설정
    @Override
    public void resetPw(MemberDto memberDto) {
        Optional<MemberEntity> getColumn = memberRepository.findById(memberDto.getMemId());
        MemberEntity memberEntity = getColumn.get();
        memberEntity.setPassword(memberDto.getPassword());

        memberRepository.save(memberEntity);
    }
}
