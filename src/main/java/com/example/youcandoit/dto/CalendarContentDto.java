package com.example.youcandoit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarContentDto {
    private List<String> scheduleList;
    private List<StickerDto> stickerList;
}
