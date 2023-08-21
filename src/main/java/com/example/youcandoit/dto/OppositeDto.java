package com.example.youcandoit.dto;

import com.example.youcandoit.entity.OppositeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OppositeDto {
    private Date certifyDate;
    private Integer groupNumber;
    private String memId;
    private String oppositeId;

    public OppositeEntity toEntity() {
        return OppositeEntity.builder()
                .certifyDate(certifyDate)
                .groupNumber(groupNumber)
                .memId(memId)
                .oppositeId(oppositeId)
                .build();
    }
}
