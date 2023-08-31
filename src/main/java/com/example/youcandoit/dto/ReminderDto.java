package com.example.youcandoit.dto;

import com.example.youcandoit.entity.ReminderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderDto {
    private Integer reminderNumber;
    private String memId;
    private String reminderContents;
    private String reminderDate;

    public ReminderEntity toEntity() {
        return ReminderEntity.builder()
                .reminderNumber(reminderNumber)
                .memId(memId)
                .reminderContents(reminderContents)
                .reminderDate(reminderDate)
                .build();
    }
}
