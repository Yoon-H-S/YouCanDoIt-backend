package com.example.youcandoit.entity;

import com.example.youcandoit.dto.ReminderDto;
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
@Table(name = "reminder") // db의 해당 테이블과 연결
@DynamicInsert
@DynamicUpdate
public class ReminderEntity {
    @Id
    @Column(name = "reminder_number")
    private Integer reminderNumber;
    @Column(name = "mem_id")
    private String memId;
    @Column(name = "reminder_contents")
    private String reminderContents;
    @Column(name = "reminder_date")
    private String reminderDate;

    public ReminderDto toDto() {
        return ReminderDto.builder()
                .reminderNumber(reminderNumber)
                .memId(memId)
                .reminderContents(reminderContents)
                .reminderDate(reminderDate)
                .build();
    }
}
