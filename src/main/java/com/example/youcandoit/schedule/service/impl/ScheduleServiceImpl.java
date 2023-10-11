package com.example.youcandoit.schedule.service.impl;

import com.example.youcandoit.dto.*;
import com.example.youcandoit.entity.ScheduleEntity;
import com.example.youcandoit.entity.StickerEntity;
import com.example.youcandoit.repository.ScheduleRepository;
import com.example.youcandoit.repository.StickerRepository;
import com.example.youcandoit.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    ScheduleRepository scheduleRepository;
    StickerRepository stickerRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, StickerRepository stickerRepository) {
        this.scheduleRepository = scheduleRepository;
        this.stickerRepository = stickerRepository;
    }

    /** 일정 생성하기 */
    @Override
    public void addSchedule(ScheduleDto scheduleDto) {
        ScheduleEntity scheduleEntity = scheduleDto.toEntity();
        // db에 insert
        scheduleRepository.save(scheduleEntity);
    }

    /** 스케줄러 오늘의 일정 */
    @Override
    public List<TodayScheduleDto> dailySchedule(String loginId) {
        Date date = Date.valueOf(LocalDate.now());
        List<TodayScheduleDto> dailyScheduleList = scheduleRepository.findSchedule(loginId, date);

        return dailyScheduleList;
    }

    /** 타임테이블 */
    @Override
    public List<TimeTableDto> getTimeTable(String loginId) {
        Date date = Date.valueOf(LocalDate.now());
        List<ScheduleTimeDto> timeList = scheduleRepository.findTime(loginId, date);

        List<TimeTableDto> timeTableList = new ArrayList<>();
        for(ScheduleTimeDto dto : timeList) {
            // 하루종일인 일정은 타임테이블 표시x
            if(dto.getStartHour() == 00 && dto.getStartMinute() == 00
                    && dto.getEndHour() == 23 && dto.getEndMinute() == 59) {
                continue;
            }

            for(int i = dto.getStartHour(); i <= dto.getEndHour(); i++) {
                TimeTableDto timeTable = new TimeTableDto();
                // 시간지정
                timeTable.setHour(i);

                // startTime 구하기
                if(i == dto.getStartHour()) { // i가 시작시간이면(반복문의 첫번째면) 시작분으로 지정
                    timeTable.setStartTime(dto.getStartMinute());
                } else { // 시작시간이 아니라면 0으로 지정
                    timeTable.setStartTime(0);
                }

                // minute 구하기
                if(dto.getStartHour() == dto.getEndHour()) { // 시작시간과 종료시간이 같다면 종료분에서 시작분 빼기
                    timeTable.setMinute(dto.getEndMinute() - dto.getStartMinute());
                } else if(i == dto.getEndHour()) { // i가 종료시간이면(반복문의 마지막이면) 종료분으로 지정
                    timeTable.setMinute(dto.getEndMinute());
                } else { // 종료시간이 아니라면 60 지정
                    timeTable.setMinute(60);
                }

                timeTableList.add(timeTable);
            }
        }

        return timeTableList;
    }

    /** 일정 체크 */
    @Override
    public void scheduleSuccess(int number, String success) {
        ScheduleEntity entity = scheduleRepository.findById(number).get();

        entity.setScheduleSuccess(success);
        scheduleRepository.save(entity);
    }

    /** 스케줄러 다가오는 일정 */
    @Override
    public List<Object[]> onComingSchedule(String loginId) {
        String startDate = LocalDate.now().plusDays(1) + " 00:00:00";
        String endDate = LocalDate.now().plusDays(7) + " 23:59:59";

        List<OnComingScheduleDto> onComingScheduleList = scheduleRepository.findOnComingSchedule(loginId, startDate, endDate);

        if(onComingScheduleList.isEmpty()) {
            System.out.println("일주일 내 스케줄이 없으므로 한달 내 스케줄 조회");
            endDate = LocalDate.now().plusMonths(1) + " 23:59:59";
            onComingScheduleList = scheduleRepository.findOnComingSchedule(loginId, startDate, endDate);
            if(onComingScheduleList.isEmpty())
                return null;
        }

        // 같은 날끼리 묶기
        List<Object[]> groupSchedule = new ArrayList<>();
        List<OnComingScheduleDto> temporary = new ArrayList<>();
        String date = onComingScheduleList.get(0).getStartDate();
        for(OnComingScheduleDto schedule : onComingScheduleList) {
            if(date.equals(schedule.getStartDate())) {
                temporary.add(schedule);
            } else {
                groupSchedule.add(temporary.toArray());
                temporary.clear();
                temporary.add(schedule);
            }
            date = schedule.getStartDate();
        };
        groupSchedule.add(temporary.toArray());

        return groupSchedule;
    }

    /** 캘린더 일정, 스티커 */
    @Override
    public CalendarContentDto schedulerCalendar(String loginId, YearMonth month) {
        LocalDate startDate = LocalDate.parse(month +"-01"); // a = 받아 오는 월
        int startWeek = startDate.getDayOfWeek().getValue();
        if(startWeek != 7) {
            startDate = startDate.minusDays(startWeek);
        }
        LocalDate endDate = startDate.plusWeeks(6).minusDays(1);

        System.out.println("시작날짜 : " + startDate);
        System.out.println("끝날짜 : " + endDate);

        // 일정 날짜 조회
        List<String> scheduleList = scheduleRepository.findCalendar(loginId, startDate.toString(), endDate.toString());
        // 스티커 조회
        List<StickerEntity> stickerEntities = stickerRepository.findStickerDateColor(loginId, startDate, endDate);

        // 스티커 dto 변환
        List<StickerDto> stickerDtoList = new ArrayList<>();
        for (StickerEntity stickerEntity : stickerEntities) {
            stickerDtoList.add(stickerEntity.toDto());
        }

        CalendarContentDto calendarContentDto = new CalendarContentDto(scheduleList, stickerDtoList);

        return calendarContentDto;
    }
}
