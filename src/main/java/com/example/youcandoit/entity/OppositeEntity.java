package com.example.youcandoit.entity;


import com.example.youcandoit.dto.OppositeDto;
import com.example.youcandoit.entity.Id.OppositeId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Date;

@Entity // 이 클래스는 엔티티이다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "opposite") // db의 해당 테이블과 연결
@DynamicInsert
@DynamicUpdate
@IdClass(OppositeId.class)
public class OppositeEntity {
    @Id
    @Column(name = "certify_date")
    private Date certifyDate;
    @Id
    @Column(name = "group_number")
    private Integer groupNumber;
    @Id
    @Column(name = "mem_id")
    private String memId;
    @Id
    @Column(name = "opposite_id")
    private String oppositeId;

    public OppositeDto toDto() {
        return OppositeDto.builder()
                .certifyDate(certifyDate)
                .groupNumber(groupNumber)
                .memId(memId)
                .oppositeId(oppositeId)
                .build();
    }
}
