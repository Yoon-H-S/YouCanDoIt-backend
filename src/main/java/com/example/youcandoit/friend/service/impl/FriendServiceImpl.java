package com.example.youcandoit.friend.service.impl;

import com.example.youcandoit.friend.dto.FriendDto;
import com.example.youcandoit.friend.entity.FriendEntity;
import com.example.youcandoit.friend.repository.FriendRepository;
import com.example.youcandoit.friend.service.FriendService;
import com.example.youcandoit.member.dto.MemberDto;
import com.example.youcandoit.member.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FriendServiceImpl implements FriendService {
    FriendRepository friendRepository;

    @Autowired // 자동 연결
    public FriendServiceImpl(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }


    // 친구 추가
    @Override
    public void plusFriend(FriendDto friendDto) {
        // Dto -> Entity로 변환
        FriendEntity friendEntity = friendDto.toEntity();
        // db에 insert
        friendRepository.save(friendEntity);
    }

    // 친구아이디로 컬럼 조회(중복 친구 방지)
    @Override
    public FriendDto getId(String friendId) {
            // db에서 값받아서 Entity에 저장
            Optional<FriendEntity> getColumn = friendRepository.findById(friendId);

            if(getColumn.isEmpty())
                return null;
            else
                return getColumn.get().toDto(); // Entity -> Dto로 변환
        }


        // 친구 목록
    @Override
    public FriendDto listfriend(FriendDto friendDto) {
        // 회원이 입력한 친구아이디를 DB에서 조회
        Optional<FriendEntity> getColumn = friendRepository.findById(friendDto.getFriendId());

        if(getColumn.isPresent()) { // 조회 결과가 있다(친구 목록 출력)
            return getColumn.get().toDto();
        } else { // 조회 결과가 없다
            return null;
        }
    }
}


