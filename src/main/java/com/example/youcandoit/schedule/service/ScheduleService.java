package com.example.youcandoit.schedule.service;

import com.example.youcandoit.dto.ScheduleDto;

import java.time.YearMonth;
import java.util.List;

public interface ScheduleService {

    void addSchedule(ScheduleDto scheduleDto);

    List<Object[]> timeTableDailySchedule(String loginId);

    List<Object[]> schedulerOnComingSchedule(String loginId);

    List<String[]> mainSchedulerCalender(String loginId, YearMonth month);

    List<Object[]> stickerDateColor(String loginId, YearMonth month);
}
