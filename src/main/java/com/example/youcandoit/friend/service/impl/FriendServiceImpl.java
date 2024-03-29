package com.example.youcandoit.friend.service.impl;

import com.example.youcandoit.dto.FriendDto;
import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.entity.FriendEntity;
import com.example.youcandoit.entity.Id.FriendId;
import com.example.youcandoit.repository.FriendRepository;
import com.example.youcandoit.friend.service.FriendService;
import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendServiceImpl implements FriendService {
    FriendRepository friendRepository;
    MemberRepository memberRepository;

    @Autowired // 자동 연결
    public FriendServiceImpl(FriendRepository friendRepository, MemberRepository memberRepository) {
        this.friendRepository = friendRepository;
        this.memberRepository = memberRepository;
    }

    /** 친구목록의 내 프로필 */
    public MemberDto myProfile(String loginId) {
        Optional<MemberEntity> getRow = memberRepository.findById(loginId);
        return MemberDto.builder().profilePicture(getRow.get().getProfilePicture()).nickname(getRow.get().getNickname()).build();
    }

    /** 친구 목록 */
    @Override
    public List<MemberDto> friendList(String loginId) {
        List<MemberEntity> getRow = friendRepository.findFriendList(loginId);

        if(getRow.isEmpty()) { // 조회 결과가 없다
            return null;
        } else { // 조회 결과가 있다.
            List<MemberDto> friends = new ArrayList<MemberDto>();
            for (MemberEntity row : getRow) {
                friends.add(MemberDto.builder().memId(row.getMemId()).nickname(row.getNickname()).profilePicture(row.getProfilePicture()).build());
            }
            return friends;
        }
    }

    /** 친구 목록 검색 */
    @Override
    public List<MemberDto> findFriends(String loginId, String friendName) {
        // 회원이 입력한 친구아이디를 DB에서 조회
        List<MemberEntity> getRow = friendRepository.findSearchFriends(loginId, friendName);

        if(getRow.isEmpty()) { // 조회 결과가 없다
            return null;
        } else { // 조회 결과가 있다.
            List<MemberDto> friends = new ArrayList<MemberDto>();
            for (MemberEntity row : getRow) {
                friends.add(MemberDto.builder().memId(row.getMemId()).nickname(row.getNickname()).profilePicture(row.getProfilePicture()).build());
            }
            return friends;
        }
    }

    /** 친구 상세 프로필(같이 있는 그룹) */
    @Override
    public List<Object> friendProfile(FriendDto friendDto) {
        Optional<MemberEntity> getRow1 = memberRepository.findById(friendDto.getFriendId());
        List<Object[]> getRow2 = friendRepository.findWithGroup(friendDto.getMemId(), friendDto.getFriendId());

        List<Object> friendProfile = new ArrayList<Object>();

        friendProfile.add(MemberDto.builder().nickname(getRow1.get().getNickname()).profilePicture(getRow1.get().getProfilePicture()).build());
        friendProfile.add(getRow2);

        System.out.println(friendProfile);

        return friendProfile;
    }

    /** 친구추가 페이지의 검색 */
    @Override
    public List<Object> searchId(FriendDto friendDto) {
        // 회원테이블에 회원이 있는지 조회
        Optional<MemberEntity> getMemberRow = memberRepository.findById(friendDto.getFriendId());

        List<Object> response = new ArrayList<Object>();
        if(getMemberRow.isPresent()) { // 회원이 있다면
            // 친구인지 조회
            FriendId friendId = new FriendId(friendDto.getMemId(), friendDto.getFriendId());
            boolean isFriend = friendRepository.existsById(friendId);

            if(isFriend) { // 친구라면
                response.add("2"); // 이미 친구인 상태
            } else { // 친구가 아니라면
                response.add("1"); // 친구가 아닌 상태
            }

            response.add(MemberDto.builder()
                    .memId(getMemberRow.get().getMemId())
                    .profilePicture(getMemberRow.get().getProfilePicture())
                    .nickname(getMemberRow.get().getNickname())
                    .build()); // dto로 변환
            return response;
        } else { // 회원이 없다면
            return null;
        }
    }

    /** 친구 추가 */
    @Override
    public void addFriend(FriendDto friendDto) {
        // Dto -> Entity로 변환
        FriendEntity friendEntity = friendDto.toEntity();
        // db에 insert
        friendRepository.save(friendEntity);
    }
}


