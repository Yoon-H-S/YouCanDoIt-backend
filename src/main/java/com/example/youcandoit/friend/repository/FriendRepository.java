package com.example.youcandoit.friend.repository;


import com.example.youcandoit.friend.entity.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<FriendEntity, String> { // <>안에는 Entity, Primarykey를 넣어준다.

}
