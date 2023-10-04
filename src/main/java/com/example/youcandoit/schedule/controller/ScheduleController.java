package com.example.youcandoit.schedule.controller;

import com.example.youcandoit.dto.FriendDto;
import com.example.youcandoit.dto.GroupDto;
import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.dto.ScheduleDto;
import com.example.youcandoit.schedule.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /** 메인페이지 오늘의 일정 스케줄러 타임 테이블, 오늘의 일정 */
    @PostMapping("/timeTable-daily-schedule")
    public List<Object[]> timeTableDailySchedule(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        List<Object[]> timeTableDailySchedule = scheduleService.timeTableDailySchedule(loginId);
        return timeTableDailySchedule;
    }

    /** 스케줄러 다가오는 일정 */
    @PostMapping("/scheduler-onComing-schedule")
    public List<Object[]> schedulerOnComingSchedule(HttpSession session) {
        String loginId = (String)session.getAttribute("loginId");
        List<Object[]> schedulerOnComingSchedule = scheduleService.schedulerOnComingSchedule(loginId);
        return schedulerOnComingSchedule;
    }
}

