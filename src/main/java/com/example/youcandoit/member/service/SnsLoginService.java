package com.example.youcandoit.member.service;

import java.util.*;

public interface SnsLoginService {

    /** 토큰 발급 */
    String getAccessToken(String code, String registrationId);

    /** 유저 정보 받기 */
    HashMap<String, Object> getResource(String accessToken, String registrationId);

}
