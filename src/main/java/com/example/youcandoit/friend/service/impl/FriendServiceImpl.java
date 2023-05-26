package com.example.youcandoit.friend.service.impl;

import com.example.youcandoit.dto.FriendDto;
import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.entity.FriendEntity;
import com.example.youcandoit.friend.repository.FriendRepository;
import com.example.youcandoit.friend.service.FriendService;
import com.example.youcandoit.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
    FriendRepository friendRepository;

    @Autowired // 자동 연결
    public FriendServiceImpl(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    @Override
    public List<MemberDto> friendList(String loginId) {
        // 회원이 입력한 친구아이디를 DB에서 조회
        List<MemberEntity> getColumn = friendRepository.findFriendList(loginId);

        if(getColumn.isEmpty()) { // 조회 결과가 없다
            return null;
        } else { // 조회 결과가 있다.
            List<MemberDto> friends = new ArrayList<MemberDto>();
            for (MemberEntity column : getColumn) {
                friends.add(MemberDto.builder().nickname(column.getNickname()).profilePicture(column.getProfilePicture()).build());
            }
            return friends;
        }
    }

    // 친구 목록 검색
    @Override
    public List<MemberDto> findFriends(String loginId, String friendName) {
        // 회원이 입력한 친구아이디를 DB에서 조회
        List<MemberEntity> getColumn = friendRepository.findMyFriends(loginId, friendName);

        if(getColumn.isEmpty()) { // 조회 결과가 없다
            return null;
        } else { // 조회 결과가 있다.
            List<MemberDto> friends = new ArrayList<MemberDto>();
            for (MemberEntity column : getColumn) {
                friends.add(MemberDto.builder().nickname(column.getNickname()).profilePicture(column.getProfilePicture()).build());
            }
            return friends;
        }
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
//            Optional<FriendEntity> getColumn = friendRepository.findById(friendId);
//
//            if(getColumn.isEmpty())
//                return null;
//            else
//                return getColumn.get().toDto(); // Entity -> Dto로 변환
        return null;
        }
}


