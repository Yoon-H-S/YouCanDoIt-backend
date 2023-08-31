package com.example.youcandoit.dto;

import com.example.youcandoit.entity.DiyAccumulateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiyAccumulateDto {
    private Integer groupNumber;
    private String memId;
    private Integer certifyCount;
    private Integer diyaccuRank;

    public DiyAccumulateEntity toEntity() {
        return DiyAccumulateEntity.builder()
                .groupNumber(groupNumber)
                .memId(memId)
                .certifyCount(certifyCount)
                .diyaccuRank(diyaccuRank)
                .build();
    }
}
