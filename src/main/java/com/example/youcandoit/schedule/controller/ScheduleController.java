package com.example.youcandoit.schedule.controller;

import com.example.youcandoit.dto.*;
import com.example.youcandoit.schedule.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("api/schedule-api")
public class ScheduleController {
    ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /** 일정 생성하기 */
    @PostMapping("/schedule-create")
    public void scheduleCreate(@RequestBody ScheduleDto scheduleDto, HttpSession session) {
        scheduleDto.setMemId((String)session.getAttribute(("loginId")));
        scheduleService.addSchedule(scheduleDto);
    }

    /** 타임테이블 */
    @GetMapping("/time-table")
    public List<TimeTableDto> timeTable(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        return scheduleService.getTimeTable(loginId);
    }

    /** 메인페이지 오늘의 일정 스케줄러, 오늘의 일정 */
    @GetMapping("/daily-schedule")
    public List<TodayScheduleDto> dailySchedule(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        return scheduleService.dailySchedule(loginId);
    }

    /** 일정 체크 */
    @GetMapping("/schedule-success")
    public void scheduleSuccess(@RequestParam int number, @RequestParam String success) {
        scheduleService.scheduleSuccess(number, success);
    }

    /** 스케줄러 다가오는 일정 */
    @GetMapping("/onComing-schedule")
    public List<Object[]> onComingSchedule(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        return scheduleService.onComingSchedule(loginId);
    }

    /** 캘린더 일정, 스티커 */
    @GetMapping("/calender-scheduler")
    public CalendarContentDto schedulerCalender(HttpSession session, @RequestParam("month") YearMonth month) {
        String loginId = (String)session.getAttribute("loginId");
        CalendarContentDto calendarContent = scheduleService.schedulerCalendar(loginId, month);
        return calendarContent;
    }
}

