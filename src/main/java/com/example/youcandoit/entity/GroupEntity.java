package com.example.youcandoit.entity;

import com.example.youcandoit.dto.GroupDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "challenge_group") // db의 해당 테이블과 연결
@DynamicInsert
@DynamicUpdate
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_number")
    private Integer groupNumber;
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "group_subject")
    private String groupSubject;
    @Column(name = "group_image")
    private String groupImage;
    @Column(name = "group_contents")
    private String groupContents;
    @Column(name = "group_startdate")
    private Date groupStartdate;
    @Column(name = "group_enddate")
    private Date groupEnddate;
    @Column(name = "group_headcount")
    private Integer groupHeadcount;
    @Column(name = "group_class")
    private String groupClass;
    @Column(name = "group_state")
    private String groupState;

    public GroupDto toDto() {
        return GroupDto.builder()
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
