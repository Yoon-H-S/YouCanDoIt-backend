package com.example.youcandoit.repository;

import com.example.youcandoit.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, String> { // <>안에는 Entity, Primarykey를 넣어준다.
}
