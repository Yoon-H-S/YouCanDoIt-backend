package com.example.youcandoit.dto;

import com.example.youcandoit.entity.MemberEntity;
import lombok.*;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private String memId;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String profilePicture;
    private Date joinDate;
    private String memClass;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .memId(memId)
                .password(password)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .profilePicture(profilePicture)
                .joinDate(joinDate)
                .memClass(memClass)
                .build();
    }
}
