package com.example.youcandoit.member.service.impl;

import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.entity.GodlifeGoalEntity;
import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.member.service.MemberService;
import com.example.youcandoit.repository.GodlifeGoalRepository;
import com.example.youcandoit.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    MemberRepository memberRepository;
    GodlifeGoalRepository godlifeGoalRepository;

    @Autowired // 자동 연결
    public MemberServiceImpl(MemberRepository memberRepository, GodlifeGoalRepository godlifeGoalRepository) {
        this.memberRepository = memberRepository;
        this.godlifeGoalRepository = godlifeGoalRepository;
    }

    /** 로그인 */
    @Override
    public MemberDto loginMember(MemberDto memberDto){
        // 회원이 입력한 아이디와 비밀번호로 DB에서 조회
        Optional<MemberEntity> getRow = memberRepository.findByPasswordAndMemIdAndMemClass(memberDto.getPassword() ,memberDto.getMemId(), memberDto.getMemClass());

        if(getRow.isPresent()) { // 조회 결과가 있다(해당 아이디을 가진 회원 정보가 있다)
            return getRow.get().toDto();
        } else { // 조회 결과가 없다(해당 아이디를 가진 회원 정보가 없다.
            return null;
        }
    }

    /** 중복아이디 확인(회원가입) */
    @Override
    public MemberDto getId(String memId) {
        // db에서 값받아서 Entity에 저장
        Optional<MemberEntity> getRow = memberRepository.findById(memId);

        if(getRow.isEmpty())
            return null;
        else
            return getRow.get().toDto(); // Entity -> Dto로 변환
    }

    /** 회원가입 */
    @Override
    public void saveMember(MemberDto memberDto) {
        // Dto -> Entity로 변환
        MemberEntity memberEntity = memberDto.toEntity();
        // db에 insert
        memberRepository.save(memberEntity);

        godlifeGoalRepository.save(GodlifeGoalEntity.builder().memId(memberEntity.getMemId()).goalPedometer(10000).build());
    }

    /** 프로필사진경로 저장 */
    @Override
    public void saveProfile(MemberDto memberDto) {
        Optional<MemberEntity> getRow = memberRepository.findById(memberDto.getMemId());
        MemberEntity memberEntity = getRow.get();
        memberEntity.setProfilePicture(memberDto.getProfilePicture());

        memberRepository.save(memberEntity);
    }

    /** 전화번호로 아이디 찾기 */
    @Override
    public MemberDto findId(MemberDto memberDto) {
        Optional<MemberEntity> getRow = memberRepository.findByPhoneNumber(memberDto.getPhoneNumber());

        if(getRow.isEmpty()) {
            return null;
        } else {
            return getRow.get().toDto(); // Entity -> Dto로 변환
        }
    }

    /** 비밀번호 찾기(아이디 & 전화번호) */
    @Override
    public MemberDto findPw(MemberDto memberDto) {
        Optional<MemberEntity> getRow = memberRepository.findByPhoneNumberAndMemId(memberDto.getPhoneNumber(), memberDto.getMemId());

        if(getRow.isEmpty()) {
            return null;
        } else {
            return getRow.get().toDto(); // Entity -> Dto로 변환
        }
    }

    /** 비밀번호 재설정 */
    @Override
    public void resetPw(MemberDto memberDto) {
        Optional<MemberEntity> getRow = memberRepository.findById(memberDto.getMemId());
        MemberEntity memberEntity = getRow.get();
        memberEntity.setPassword(memberDto.getPassword());

        memberRepository.save(memberEntity);
    }


}
