package com.example.youcandoit.dto;

import com.example.youcandoit.entity.FriendEntity;
import com.example.youcandoit.entity.MemberEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendDto {
    private String memId;
    private String friendId;

    public FriendEntity toEntity() {
        return FriendEntity.builder()
                .memId(memId)
                .friendId(friendId)
                .build();
    }
}
