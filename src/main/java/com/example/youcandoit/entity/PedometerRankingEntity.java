package com.example.youcandoit.entity;

import com.example.youcandoit.dto.PedometerAccumulateDto;
import com.example.youcandoit.dto.PedometerRankingDto;
import com.example.youcandoit.entity.Id.GroupPersonId;
import com.example.youcandoit.entity.Id.PedometerRankingId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Date;

@Entity // 이 클래스는 엔티티이다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pedometer_ranking") // db의 해당 테이블과 연결
@DynamicUpdate
@IdClass(PedometerRankingId.class)
public class PedometerRankingEntity {
    @Id
    @Column(name = "pedometer_date")
    private Date pedometerDate;
    @Id
    @Column(name = "group_number")
    private Integer groupNumber;
    @Id
    @Column(name = "mem_id")
    private String memId;
    @Column(name = "pedometer_result")
    private Integer pedometerResult;
    @Column(name = "pedometer_rank")
    private Integer pedometerRank;

    public PedometerRankingDto toDto() {
        return PedometerRankingDto.builder()
                .pedometerDate(pedometerDate)
                .groupNumber(groupNumber)
                .memId(memId)
                .pedometerResult(pedometerResult)
                .pedometerRank(pedometerRank)
                .build();
    }
}
