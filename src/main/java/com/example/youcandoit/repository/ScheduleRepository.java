package com.example.youcandoit.repository;

import com.example.youcandoit.dto.OnComingScheduleDto;
import com.example.youcandoit.dto.ScheduleTimeDto;
import com.example.youcandoit.dto.TodayScheduleDto;
import com.example.youcandoit.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Integer> {

    /** 메인페이지 오늘의 일정, 스케줄러 오늘의 일정*/
    @Query(value = "select s.scheduleNumber as number, " +
            "s.scheduleTitle as title, " +
            "date_format(s.scheduleStartDate, '%H:%i') as startTime, " +
            "date_format(s.scheduleEndDate, '%H:%i') as endTime, " +
            "s.scheduleSuccess as success " +
            "from ScheduleEntity s " +
            "where s.memId = :loginId and s.scheduleStartDate like :date% " +
            "order by s.scheduleStartDate")
    List<TodayScheduleDto> findSchedule(@Param("loginId")String loginId, @Param("date") Date date);

    /** 타임테이블 */
    @Query(value = "select date_format(s.scheduleStartDate, '%H') as startHour, " +
            "date_format(s.scheduleStartDate, '%i') as startMinute, " +
            "date_format(s.scheduleEndDate, '%H') as endHour, " +
            "date_format(s.scheduleEndDate, '%i') as endMinute " +
            "from ScheduleEntity s " +
            "where s.memId = :loginId and s.scheduleStartDate like :date% " +
            "order by s.scheduleStartDate")
    List<ScheduleTimeDto> findTime(@Param("loginId")String loginId, @Param("date") Date date);

    /** 다가오는 일정 */
    @Query(value = "select s.scheduleTitle as title, " +
            "date_format(s.scheduleStartDate, '%m/%d') as startDate, " +
            "date_format(s.scheduleStartDate, '%H:%i') as startTime, " +
            "date_format(s.scheduleEndDate, '%H:%i') as endTime " +
            "from ScheduleEntity s " +
            "where s.memId = :loginId and s.scheduleStartDate between :startDate and :endDate " +
            "order by s.scheduleStartDate")
    List<OnComingScheduleDto> findOnComingSchedule(@Param("loginId")String loginId, @Param("startDate")String startDate, @Param("endDate")String endDate);

    @Query(value = "select * from schedule where date_sub(schedule_startdate, interval schedule_reminder minute)=:now", nativeQuery = true)
    List<ScheduleEntity> findScheduleToReminder(@Param("now")String now);

    /** 메인 캘린더, 스케줄러 캘린더 일정 */
    @Query(value = "select date_format(s.scheduleStartDate, '%Y-%m-%d') from ScheduleEntity s " +
            "where s.memId=:loginId and s.scheduleStartDate between :sDate and :eDate " +
            "order by s.scheduleStartDate")
    List<String> findCalendar(@Param("loginId")String loginId, @Param("sDate")String sDate, @Param("eDate")String eDate);
}
