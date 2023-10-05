package com.example.youcandoit.repository;

import com.example.youcandoit.entity.DiyCertifyEntity;
import com.example.youcandoit.entity.GroupEntity;
import com.example.youcandoit.entity.MemberEntity;
import com.example.youcandoit.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Integer> {

    /** 메인페이지 오늘의 일정, 스케줄러 타임테이블, 스케줄러 오늘의 일정*/
    @Query(value = "select s from ScheduleEntity s " +
            "where s.memId = :loginId and s.scheduleStartDate like :date% " +
            "order by s.scheduleStartDate")
    List<ScheduleEntity> findSchedule(@Param("loginId")String loginId, @Param("date") Date date);

    /** 다가오는 일정 */
    @Query(value = "select s from ScheduleEntity s " +
            "where s.memId = :loginId and s.scheduleStartDate >:date " +
            "order by s.scheduleStartDate")
    List<ScheduleEntity> findOnComingSchedule(@Param("loginId")String loginId ,@Param("date")String date);

    @Query(value = "select * from schedule where date_sub(schedule_startdate, interval schedule_reminder minute)=:now", nativeQuery = true)
    List<ScheduleEntity> findScheduleToReminder(@Param("now")String now);

    /** 메인 캘린더, 스케줄러 캘린더 제목 */
    @Query(value = "select s from ScheduleEntity s " +
            "where s.memId=:loginId and s.scheduleStartDate>=:sDate and s.scheduleStartDate<=:eDate " +
            "order by s.scheduleStartDate")
    List<ScheduleEntity> findCalender(@Param("loginId")String loginId, @Param("sDate")String sDate, @Param("eDate")String eDate);
}
