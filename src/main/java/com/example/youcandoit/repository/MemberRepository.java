package com.example.youcandoit.repository;

import com.example.youcandoit.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> { // <>안에는 Entity, Primarykey를 넣어준다.
    Optional<MemberEntity> findByPasswordAndMemIdAndMemClass(String password, String memId, String memClass);
    Optional<MemberEntity> findByPhoneNumber(String phoneNumber);
    Optional<MemberEntity> findByPhoneNumberAndMemId(String phoneNumber, String memId);
}
