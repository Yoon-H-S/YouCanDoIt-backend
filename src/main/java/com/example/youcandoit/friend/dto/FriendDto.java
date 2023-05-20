package com.example.youcandoit.friend.dto;

import com.example.youcandoit.friend.entity.FriendEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
