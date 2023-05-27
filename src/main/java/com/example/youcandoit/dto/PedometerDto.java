package com.example.youcandoit.dto;

import com.example.youcandoit.entity.PedometerEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedometerDto {
    private Date pedometerDate;
    private String memId;
    private Integer pedometerResult;

    public PedometerEntity toEntity() {
        return PedometerEntity.builder()
                .pedometerDate(pedometerDate)
                .memId(memId)
                .pedometerResult(pedometerResult)
                .build();
    }
}
