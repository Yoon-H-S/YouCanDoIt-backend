package com.example.youcandoit.repository;

import com.example.youcandoit.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> { // <>안에는 Entity, Primarykey를 넣어준다.
    /** 로그인(아이디&비밀번호 일치여부) */
    Optional<MemberEntity> findByPasswordAndMemIdAndMemClass(String password, String memId, String memClass);

    /** 전화번호로 아이디 찾기 */
    Optional<MemberEntity> findByPhoneNumber(String phoneNumber);

    /** 비밀번호 찾기(아이디 & 전화번호) */
    Optional<MemberEntity> findByPhoneNumberAndMemId(String phoneNumber, String memId);
}
