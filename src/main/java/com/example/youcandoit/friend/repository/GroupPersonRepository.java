package com.example.youcandoit.friend.repository;


import com.example.youcandoit.entity.GroupPersonEntity;
import com.example.youcandoit.entity.Id.GroupPersonId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupPersonRepository extends JpaRepository<GroupPersonEntity, GroupPersonId> { // <>안에는 Entity, Primarykey를 넣어준다.
}
