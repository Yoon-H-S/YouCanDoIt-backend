package com.example.youcandoit.entity;


import com.example.youcandoit.dto.DiyCertifyDto;
import com.example.youcandoit.entity.Id.DiyCertifyId;
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
@Table(name = "diy_certify") // db의 해당 테이블과 연결
@DynamicInsert
@DynamicUpdate
@IdClass(DiyCertifyId.class)
public class DiyCertifyEntity {
    @Id
    @Column(name = "certify_date")
    private Date certifyDate;
    @Id
    @Column(name = "group_number")
    private Integer groupNumber;
    @Id
    @Column(name = "mem_id")
    private String memId;
    @Column(name = "certify_image")
    private String certifyImage;
    @Column(name = "opposite_count")
    private Integer oppositeCount;

    public DiyCertifyDto toDto() {
        return DiyCertifyDto.builder()
                .certifyDate(certifyDate)
                .groupNumber(groupNumber)
                .memId(memId)
                .certifyImage(certifyImage)
                .oppositeCount(oppositeCount)
                .build();
    }
}
