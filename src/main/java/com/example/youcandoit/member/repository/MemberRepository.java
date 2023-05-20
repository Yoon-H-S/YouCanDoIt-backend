package com.example.youcandoit.member.repository;

import com.example.youcandoit.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> { // <>안에는 Entity, Primarykey를 넣어준다.
    Optional<MemberEntity> findByPasswordAndMemId(String password, String memId);
    Optional<MemberEntity> findByPhoneNumber(String phoneNumber);
    Optional<MemberEntity> findByPhoneNumberAndMemId(String phoneNumber, String memId);
}
