package com.example.youcandoit.entity;

import com.example.youcandoit.dto.ScheduleDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity // 이 클래스는 엔티티이다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "schedule") // db의 해당 테이블과 연결
@DynamicInsert
@DynamicUpdate
public class ScheduleEntity {
    @Id
    @Column(name = "schedule_number")
    private Integer scheduleNumber;
    @Column(name = "mem_id")
    private String memId;
    @Column(name = "schedule_title")
    private String scheduleTitle;
    @Column(name = "schedule_startdate")
    private String scheduleStartDate;
    @Column(name = "schedule_enddate")
    private String scheduleEndDate;
    @Column(name = "schedule_reminder")
    private Integer scheduleReminder;
    @Column(name = "schedule_repeat")
    private String scheduleRepeat;
    @Column(name = "schedule_success")
    private String scheduleSuccess;

    public ScheduleDto toDto() {
        return ScheduleDto.builder()
                .scheduleNumber(scheduleNumber)
                .memId(memId)
                .scheduleTitle(scheduleTitle)
                .scheduleStartDate(scheduleStartDate)
                .scheduleEndDate(scheduleEndDate)
                .scheduleReminder(scheduleReminder)
                .scheduleRepeat(scheduleRepeat)
                .scheduleSuccess(scheduleSuccess)
                .build();
    }
}
