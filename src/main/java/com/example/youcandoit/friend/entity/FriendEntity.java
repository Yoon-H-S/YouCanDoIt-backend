package com.example.youcandoit.friend.entity;

import com.example.youcandoit.friend.dto.FriendDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity // 이 클래스는 엔티티이다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "friend") // db의 해당 테이블과 연결
@DynamicUpdate
public class FriendEntity {
    @Id
    @Column(name = "mem_id")
    String memId;
    @Column(name = "friend_id")
    String friendId;

    public FriendDto toDto() {
        return FriendDto.builder()
                .memId(memId)
                .friendId(friendId)
                .build();
    }
}
