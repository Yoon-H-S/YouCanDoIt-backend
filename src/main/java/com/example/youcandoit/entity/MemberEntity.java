package com.example.youcandoit.entity;

import com.example.youcandoit.dto.MemberDto;
import jakarta.persistence.*;
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
    private String joinDate;
    @Column(name = "mem_class")
    private String memClass;

    public MemberDto toDto() {
        return MemberDto.builder()
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
