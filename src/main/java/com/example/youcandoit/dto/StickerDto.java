package com.example.youcandoit.dto;

import com.example.youcandoit.entity.StickerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StickerDto {
    private Date stickerDate;
    private String memId;
    private String stickerColor;

    public StickerEntity toEntity() {
        return StickerEntity.builder()
                .stickerDate(stickerDate)
                .memId(memId)
                .stickerColor(stickerColor)
                .build();
    }
}
