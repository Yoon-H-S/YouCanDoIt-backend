package com.example.youcandoit.schedule.service.impl;

import com.example.youcandoit.dto.ScheduleDto;
import com.example.youcandoit.entity.FriendEntity;
import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.entity.ScheduleEntity;
import com.example.youcandoit.entity.StickerEntity;
import com.example.youcandoit.repository.ScheduleRepository;
import com.example.youcandoit.repository.StickerRepository;
import com.example.youcandoit.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<Object[]> timeTableDailySchedule(String loginId) {
        Date date = Date.valueOf(LocalDate.now());
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findSchedule(loginId, date);

        List<Object[]> dailySchedule = new ArrayList<Object[]>();
        for (ScheduleEntity scheduleEntity : scheduleEntities) {
            dailySchedule.add(
                    new Object[]{scheduleEntity.getScheduleStartDate().substring(11, 16),
                            scheduleEntity.getScheduleEndDate().substring(11, 16),
                            scheduleEntity.getScheduleTitle(),
                            scheduleEntity.getScheduleSuccess()});
        }

        return dailySchedule;
    }

    /** 스케줄러 다가오는 일정 */
    @Override
    public List<Object[]> schedulerOnComingSchedule(String loginId) {
        String startDate = LocalDate.now().plusDays(1) + " 00:00:00";
        String endDate = LocalDate.now().plusDays(7) + " 23:59:59";

        List<ScheduleEntity> scheduleEntities = scheduleRepository.findOnComingSchedule(loginId, startDate, endDate);

        if(scheduleEntities.isEmpty()) {
            System.out.println("일주일 내 스케줄이 없으므로 한달 내 스케줄 조회");
            endDate = LocalDate.now().plusMonths(1) + " 23:59:59";
            scheduleEntities = scheduleRepository.findOnComingSchedule(loginId, startDate, endDate);
        }

        List<Object[]> onComingSchedule = new ArrayList<Object[]>();
        for (ScheduleEntity scheduleEntity : scheduleEntities) {
            onComingSchedule.add(
                    new Object[]{scheduleEntity.getScheduleStartDate().substring(5, 16),
                            scheduleEntity.getScheduleEndDate().substring(5, 16),
                            scheduleEntity.getScheduleTitle()});
        }

        return onComingSchedule;
    }

    /** 캘린더 제목 */
    @Override
    public List<String[]> mainSchedulerCalender(String loginId, YearMonth month) {
        LocalDate startDate = LocalDate.parse( month +"-01"); // a = 받아 오는 월
        int startWeek = startDate.getDayOfWeek().getValue();
        System.out.println(startDate + ", " + startWeek);
        if(startWeek != 7) {
            startDate = startDate.minusDays(startWeek);
            startWeek = startDate.getDayOfWeek().getValue();
            System.out.println(startDate + ", " + startWeek);
        }

        LocalDate endDate = startDate.plusWeeks(6).minusDays(1);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        String strStartDate = format.format(startDate);
//        String strEndDate = format.format(endDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String strStartDate = startDate.format(formatter);
        String strEndDate = endDate.format(formatter);
        System.out.println(strStartDate);
        System.out.println(strEndDate);

//        List<ScheduleEntity> scheduleEntities = scheduleRepository.findCalender(loginId, startDate, endDate);
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findCalender(loginId, strStartDate, strEndDate);
        System.out.println(scheduleEntities);

        List<String[]> calender = new ArrayList<String[]>();
        for (ScheduleEntity scheduleEntity : scheduleEntities) {
            calender.add(new String[]{scheduleEntity.getScheduleStartDate().substring(0,10), scheduleEntity.getScheduleTitle()});
        }
        System.out.println(calender);
        return calender;
    }

    /** 캘린더 스티커 날짜, 컬러 */
    @Override
    public List<Object[]> stickerDateColor(String loginId, YearMonth month) {
        LocalDate startDate = LocalDate.parse( month +"-01"); // a = 받아 오는 월
        int startWeek = startDate.getDayOfWeek().getValue();
        System.out.println(startDate + ", " + startWeek);
        if(startWeek != 7) {
            startDate = startDate.minusDays(startWeek);
            startWeek = startDate.getDayOfWeek().getValue();
            System.out.println(startDate + ", " + startWeek);
        }

        LocalDate endDate = startDate.plusWeeks(6).minusDays(1);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String strStartDate = startDate.format(formatter);
//        String strEndDate = endDate.format(formatter);
//        System.out.println(strStartDate);
//        System.out.println(strEndDate);

        List<StickerEntity> stickerEntities = stickerRepository.findStickerDateColor(loginId, startDate, endDate);
//        List<StickerEntity> stickerEntities = stickerRepository.findStickerDateColor(loginId, strStartDate, strEndDate);

        List<Object[]> stickerDateColor = new ArrayList<Object[]>();
        for (StickerEntity stickerEntity : stickerEntities) {
            stickerDateColor.add(new Object[]{stickerEntity.getStickerDate(), stickerEntity.getStickerColor()});
        }

        return stickerDateColor;
    }
}
