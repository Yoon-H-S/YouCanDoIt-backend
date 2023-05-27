package com.example.youcandoit.entity;

import com.example.youcandoit.dto.GroupPersonDto;
import com.example.youcandoit.entity.Id.FriendId;
import com.example.youcandoit.entity.Id.GroupPersonId;
import jakarta.persistence.*;
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
@Table(name = "Group_person") // db의 해당 테이블과 연결
@DynamicUpdate
@IdClass(GroupPersonId.class)
public class GroupPersonEntity {
    @Id
    @Column(name = "group_number")
    private Integer groupNumber;
    @Id
    @Column(name = "mem_id")
    private String memId;
    @Column(name = "person_status")
    private String personStatus;

    public GroupPersonDto toDto() {
        return GroupPersonDto.builder()
                .groupNumber(groupNumber)
                .memId(memId)
                .personStatus(personStatus)
                .build();
    }
}
