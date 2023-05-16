package com.example.youcandoit.entity;

import com.example.youcandoit.dto.MemberDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity // 이 클래스는 엔티티이다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member") // db의 해당 테이블과 연결
@DynamicUpdate
public class MemberEntity {
    @Id
    @Column(name = "mem_id")
    String memId;
    @Column(name = "password")
    String password;
    @Column(name = "nickname")
    String nickname;
    @Column(name = "phone_number")
    String phoneNumber;
    @Column(name = "profile_picture")
    String profilePicture;
    @Column(name = "join_date")
    String joinDate;

    public MemberDto toDto() {
        return MemberDto.builder()
                .memId(memId)
                .password(password)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .profilePicture(profilePicture)
                .joinDate(joinDate)
                .build();
    }
}
