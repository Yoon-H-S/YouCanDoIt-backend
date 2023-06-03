package com.example.youcandoit.entity;

import com.example.youcandoit.entity.Id.FriendId;
import com.example.youcandoit.dto.FriendDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity // 이 클래스는 엔티티이다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "friend") // db의 해당 테이블과 연결
@DynamicInsert
@DynamicUpdate
@IdClass(FriendId.class)
public class FriendEntity {
    @Id
    @Column(name = "mem_id")
    private String memId;
    @Id
    @Column(name = "friend_id")
    private String friendId;

    public FriendDto toDto() {
        return FriendDto.builder()
                .memId(memId)
                .friendId(friendId)
                .build();
    }
}
