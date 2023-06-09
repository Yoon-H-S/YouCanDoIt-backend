package com.example.youcandoit.dto;

import com.example.youcandoit.entity.GroupEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDto {
    private Integer groupNumber;
    private String groupName;
    private String groupSubject;
    private String groupImage;
    private String groupContents;
    private Date groupStartdate;
    private Date groupEnddate;
    private Integer groupHeadcount;
    private String groupClass;
    private String groupState;

    public GroupEntity toEntity() {
        return GroupEntity.builder()
                .groupNumber(groupNumber)
                .groupName(groupName)
                .groupSubject(groupSubject)
                .groupImage(groupImage)
                .groupContents(groupContents)
                .groupStartdate(groupStartdate)
                .groupEnddate(groupEnddate)
                .groupHeadcount(groupHeadcount)
                .groupClass(groupClass)
                .groupState(groupState)
                .build();
    }
}
