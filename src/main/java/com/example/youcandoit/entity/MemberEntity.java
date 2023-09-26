package com.example.youcandoit.entity;

import com.example.youcandoit.dto.MemberDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Date;

@Entity // 이 클래스는 엔티티이다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member") // db의 해당 테이블과 연결
@DynamicInsert
@DynamicUpdate
public class MemberEntity {
    @Id
    @Column(name = "mem_id")
    private String memId;
    @Column(name = "password")
    private String password;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "profile_picture")
    private String profilePicture;
    @Column(name = "join_date")
    private Date joinDate;
    @Column(name = "mem_class")
    private String memClass;
    @Column(name = "mobile_token")
    private String mobileToken;

    public MemberDto toDto() {
        return MemberDto.builder()
                .memId(memId)
                .password(password)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .profilePicture(profilePicture)
                .joinDate(joinDate)
                .memClass(memClass)
                .mobileToken(mobileToken)
                .build();
    }
}
