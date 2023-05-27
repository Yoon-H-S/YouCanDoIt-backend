package com.example.youcandoit.dto;

import com.example.youcandoit.entity.GroupPersonEntity;
import com.example.youcandoit.entity.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupPersonDto {
    private Integer groupNumber;
    private String memId;
    private String personStatus;

    public GroupPersonEntity toEntity() {
        return GroupPersonEntity.builder()
                .groupNumber(groupNumber)
                .memId(memId)
                .personStatus(personStatus)
                .build();
    }
}
