package com.example.youcandoit.dto;

import com.example.youcandoit.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private String mem_id;
    private String password;
    private String nickname;
    private String phone_number;
    private String profile_picture;

    public MemberEntity toEntity(String join_date) {
        return MemberEntity.builder()
                .mem_id(mem_id)
                .password(password)
                .nickname(nickname)
                .phone_number(phone_number)
                .profile_picture(profile_picture)
                .join_date(join_date)
                .build();
    }
}
