package com.example.youcandoit.friend.service.impl;

import com.example.youcandoit.friend.repository.FriendRepository;
import com.example.youcandoit.friend.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendServiceImpl implements FriendService {
    FriendRepository friendRepository;

    @Autowired // 자동 연결
    public FriendServiceImpl(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }
}
