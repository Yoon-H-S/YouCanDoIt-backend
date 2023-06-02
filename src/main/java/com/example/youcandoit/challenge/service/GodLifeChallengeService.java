package com.example.youcandoit.challenge.service;

import com.example.youcandoit.dto.GodlifeChallengeDto;
import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.dto.MemberDto;

import java.util.List;

public interface GodLifeChallengeService {

    List<MemberDto> withFriend(String loginId);

    GodlifeChallengeDto godLifeChallenge(GodlifeChallengeDto gcDto);

    GodlifeChallengeDto godLifeChallengeDetail(GodlifeChallengeDto gcDto);

    void saveGodlifeChallenge(GroupDto gDto);
}
