package com.example.youcandoit.schedule.service;

import com.example.youcandoit.dto.ScheduleDto;

import java.util.List;

public interface ScheduleService {

    void addSchedule(ScheduleDto scheduleDto);

    List<Object[]> mainDailySchedule(String loginId);

    List<Object[]> scheduleTimetableDailySchedule(String loginId);

    List<Object[]> onComingSchedule(String loginId);
}
