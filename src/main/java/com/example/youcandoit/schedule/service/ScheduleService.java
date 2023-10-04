package com.example.youcandoit.schedule.service;

import com.example.youcandoit.dto.ScheduleDto;

import java.util.List;

public interface ScheduleService {

    void addSchedule(ScheduleDto scheduleDto);

    List<Object[]> timeTableDailySchedule(String loginId);

    List<Object[]> schedulerOnComingSchedule(String loginId);
}
