package com.example.youcandoit.schedule.service;

import com.example.youcandoit.dto.CalendarContentDto;
import com.example.youcandoit.dto.ScheduleDto;
import com.example.youcandoit.dto.TimeTableDto;
import com.example.youcandoit.dto.TodayScheduleDto;

import java.time.YearMonth;
import java.util.List;

public interface ScheduleService {

    void addSchedule(ScheduleDto scheduleDto);

    List<TodayScheduleDto> dailySchedule(String loginId);

    List<TimeTableDto> getTimeTable(String loginId);

    void scheduleSuccess(int number, String success);

    List<Object[]> onComingSchedule(String loginId);

    CalendarContentDto schedulerCalendar(String loginId, YearMonth month);
}
