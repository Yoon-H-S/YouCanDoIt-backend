package com.example.youcandoit.dto;

import com.example.youcandoit.entity.PedometerAccumulateEntity;
import com.example.youcandoit.entity.PedometerRankingEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedometerRankingDto {
    private Date pedometerDate;
    private Integer groupNumber;
    private String memId;
    private Integer pedometerResult;
    private Integer pedometerRank;

    public PedometerRankingEntity toEntity() {
        return PedometerRankingEntity.builder()
                .pedometerDate(pedometerDate)
                .groupNumber(groupNumber)
                .memId(memId)
                .pedometerResult(pedometerResult)
                .pedometerRank(pedometerRank)
                .build();
    }
}
