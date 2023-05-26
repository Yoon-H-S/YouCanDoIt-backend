package com.example.youcandoit.entity.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedometerRankingId {
    private Date pedometerDate;
    private Integer groupNumber;
    private String memId;
}
