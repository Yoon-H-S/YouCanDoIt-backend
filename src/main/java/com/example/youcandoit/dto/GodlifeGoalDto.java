package com.example.youcandoit.dto;

import com.example.youcandoit.entity.GodlifeGoalEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GodlifeGoalDto {
    private String memId;
    private Integer goalPedometer;

    public GodlifeGoalEntity toEntity() {
        return GodlifeGoalEntity.builder()
                .memId(memId)
                .goalPedometer(goalPedometer)
                .build();
    }
}
