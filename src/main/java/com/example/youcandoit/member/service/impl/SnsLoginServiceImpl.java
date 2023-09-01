package com.example.youcandoit.member.service.impl;

import com.example.youcandoit.member.service.SnsLoginService;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.*;
import org.springframework.util.*;

@Service
public class SnsLoginServiceImpl implements SnsLoginService {
    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();

    public SnsLoginServiceImpl(Environment env) {
        this.env = env;
    }

    /** 토큰 발급 */
    public String getAccessToken(String code, String registrationId) {
        String accessToken = "";
        String tokenUri = env.getProperty("oauth2." + registrationId + ".token-uri"); // 토큰 요청을 보낼 주소
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", env.getProperty("oauth2." + registrationId + ".client-id"));
        params.add("client_secret", env.getProperty("oauth2." + registrationId + ".client-secret"));
        params.add("grant_type", "authorization_code");
        if(registrationId.equals("naver")) {
            params.add("state", "ycdi");
        } else {
            params.add("redirect_uri", env.getProperty("oauth2." + registrationId + ".redirect-uri"));
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 헤더 콘텐츠 타입 설정
            HttpEntity entity = new HttpEntity(params, headers);

            JsonNode responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class).getBody(); // 토큰 요청

            accessToken = responseNode.get("access_token").asText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    /** 유저 정보 받기 */
    @Override
    public HashMap<String, Object> getResource(String accessToken, String registrationId) {
        HashMap<String, Object> userInfo = new HashMap<String, Object>();
        String resourceUri = env.getProperty("oauth2."+registrationId+".resource-uri");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity entity = new HttpEntity(headers);
            JsonNode responseNode = restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();

            if(registrationId.equals("naver")) {
                JsonNode response = responseNode.get("response");
                userInfo.put("id", response.get("id").asText());
                userInfo.put("email", response.get("email").asText());
            } else {
                userInfo.put("id", responseNode.get("id").asText());
                if(registrationId.equals(("kakao"))) {
                    JsonNode kakaoAccount = responseNode.get("kakao_account");
                    userInfo.put("email", kakaoAccount.get("email").asText());
                } else {
                    userInfo.put("email", responseNode.get("email").asText());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }
}
