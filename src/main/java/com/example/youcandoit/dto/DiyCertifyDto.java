package com.example.youcandoit.dto;

import com.example.youcandoit.entity.DiyCertifyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiyCertifyDto {
    private Date certifyDate;
    private Integer groupNumber;
    private String memId;
    private String certifyImage;
    private Integer oppositeCount;

    public DiyCertifyEntity toEntity() {
        return DiyCertifyEntity.builder()
                .certifyDate(certifyDate)
                .groupNumber(groupNumber)
                .memId(memId)
                .certifyImage(certifyImage)
                .oppositeCount(oppositeCount)
                .build();
    }
}
