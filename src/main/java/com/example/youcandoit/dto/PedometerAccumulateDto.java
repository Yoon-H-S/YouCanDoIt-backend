package com.example.youcandoit.dto;

import com.example.youcandoit.entity.PedometerAccumulateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedometerAccumulateDto {
    private Integer groupNumber;
    private String memId;
    private Integer pedometerCount;
    private Integer pedoaccuRank;

    public PedometerAccumulateEntity toEntity() {
        return PedometerAccumulateEntity.builder()
                .groupNumber(groupNumber)
                .memId(memId)
                .pedometerCount(pedometerCount)
                .pedoaccuRank(pedoaccuRank)
                .build();
    }
}
