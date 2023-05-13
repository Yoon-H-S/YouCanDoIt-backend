package com.example.youcandoit.entity;

import com.example.youcandoit.dto.MemberDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity // 이 클래스는 엔티티이다.
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member") // db의 해당 테이블과 연결
public class MemberEntity {
    @Id
    String mem_id;
    String password;
    String nickname;
    String phone_number;
    String profile_picture;
    String join_date;

    public MemberDto toDto() {
        return MemberDto.builder()
                .mem_id(mem_id)
                .password(password)
                .nickname(nickname)
                .phone_number(phone_number)
                .profile_picture(profile_picture)
                .build();
    }
}
