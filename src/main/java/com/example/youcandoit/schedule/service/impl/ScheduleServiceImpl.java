package com.example.youcandoit.schedule.service.impl;

import com.example.youcandoit.dto.ScheduleDto;
import com.example.youcandoit.entity.FriendEntity;
import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.entity.ScheduleEntity;
import com.example.youcandoit.repository.ScheduleRepository;
import com.example.youcandoit.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
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
            dailySchedule.add(new Object[]{scheduleEntity.getScheduleStartDate().substring(11, 16), scheduleEntity.getScheduleEndDate().substring(11, 16), scheduleEntity.getScheduleTitle()});
        }

        return dailySchedule;
    }

    /** 스케줄러 다가오는 일정 */
    @Override
    public List<Object[]> schedulerOnComingSchedule(String loginId) {
        Date tomorrow = Date.valueOf(LocalDate.now().plusDays(1));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(tomorrow);

        List<ScheduleEntity> scheduleEntities = scheduleRepository.findOnComingSchedule(loginId, str);

        List<Object[]> onComingSchedule = new ArrayList<Object[]>();
        for (ScheduleEntity scheduleEntity : scheduleEntities) {
            onComingSchedule.add(new Object[]{scheduleEntity.getScheduleStartDate().substring(5, 16), scheduleEntity.getScheduleEndDate().substring(5, 16), scheduleEntity.getScheduleTitle()});
        }

        return onComingSchedule;
    }
}
