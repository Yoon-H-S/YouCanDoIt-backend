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

    /** 메인페이지 오늘의 일정 */
    @Override
    public List<Object[]> mainDailySchedule(String loginId) {
        Date date = Date.valueOf(LocalDate.now());
        List<Object[]> getRow = scheduleRepository.findScheduleList(loginId, date);
        List<Object[]> mainDailyScheduleData = new ArrayList<Object[]>();
        for(Object[] row : getRow) {
            ScheduleDto scheduleDto = ((ScheduleEntity)row[0]).toDto();
            String startDate = scheduleDto.getScheduleStartDate().substring(0,10);
            int compare = startDate.compareTo(String.valueOf(date));
            if(compare == 0) // 일정 시작날짜가 오늘와 같다면
                // 일정 제목
                mainDailyScheduleData.add(new Object[]{scheduleDto.getScheduleTitle()});
        }
        return mainDailyScheduleData;
    }

    /**  스케줄러 오늘의 일정 & 타임 테이블  */
    @Override
    public List<Object[]> scheduleTimetableDailySchedule(String loginId) {
        Date date = Date.valueOf(LocalDate.now());
        List<Object[]> getRow = scheduleRepository.findScheduleList(loginId, date);
        List<Object[]> scheduleTimetableDailyScheduleData = new ArrayList<Object[]>();
        for (Object[] row : getRow) {
            ScheduleDto scheduleDto = ((ScheduleEntity) row[0]).toDto();
            String startDate = scheduleDto.getScheduleStartDate().substring(0, 10);
            int compare = startDate.compareTo(String.valueOf(date));
            if (compare == 0) // 일정 시작날짜가 현재 날짜와 같다면
                // 일정 제목
                scheduleTimetableDailyScheduleData.add(new Object[]{scheduleDto.getScheduleTitle(), scheduleDto.getScheduleStartDate(), scheduleDto.getScheduleEndDate()});
        }
        return scheduleTimetableDailyScheduleData;
    }

    /** 스케줄러 다가오는 일정 */
    @Override
    public List<Object[]> onComingSchedule(String loginId) {
        Date date = Date.valueOf(LocalDate.now());
        List<Object[]> getRow = scheduleRepository.findScheduleList(loginId, date);
        List<Object[]> onComingScheduleData = new ArrayList<Object[]>();
        for (Object[] row : getRow) {
            ScheduleDto scheduleDto = ((ScheduleEntity) row[0]).toDto();
            String startDate = scheduleDto.getScheduleStartDate().substring(0, 10);
            int compare = startDate.compareTo(String.valueOf(date));
            if (compare > 0) // 일정의 시작날짜가 현재 날짜보다 크다면
                // 일정 제목
                onComingScheduleData.add(new Object[]{scheduleDto.getScheduleTitle(), scheduleDto.getScheduleStartDate(), scheduleDto.getScheduleEndDate()});
        }
        return onComingScheduleData;
    }
}
