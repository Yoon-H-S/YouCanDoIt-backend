package com.example.youcandoit.member.service;

import java.util.*;

public interface SnsLoginService {
//    String getKakaoAccessToken(String code, String registrationId);
//    HashMap<String, Object> getKakaoResource(String token);
    String getAccessToken(String code, String registrationId);
    HashMap<String, Object> getResource(String accessToken, String registrationId);
}
