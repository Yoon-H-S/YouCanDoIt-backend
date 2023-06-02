package com.example.youcandoit.challenge.service;

import com.example.youcandoit.dto.GroupDto;

import java.util.List;

public interface ChallengeService {
    List<GroupDto> challengeReservation(String loginId);
}
