package com.example.youcandoit.dto;

import com.example.youcandoit.entity.ScheduleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDto {
    private Integer scheduleNumber;
    private String memId;
    private String scheduleTitle;
    private String scheduleStartDate;
    private String scheduleEndDate;
    private Integer scheduleReminder;
    private String scheduleRepeat;
    private String scheduleSuccess;

    public ScheduleEntity toEntity() {
        return ScheduleEntity.builder()
                .scheduleNumber(scheduleNumber)
                .memId(memId)
                .scheduleTitle(scheduleTitle)
                .scheduleStartDate(scheduleStartDate)
                .scheduleEndDate(scheduleEndDate)
                .scheduleReminder(scheduleReminder)
                .scheduleRepeat(scheduleRepeat)
                .scheduleSuccess(scheduleSuccess)
                .build();
    }
}
