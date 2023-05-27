package com.example.youcandoit.entity;

import com.example.youcandoit.dto.PedometerDto;
import com.example.youcandoit.entity.Id.PedometerAccumulateId;
import com.example.youcandoit.entity.Id.PedometerId;
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
@Table(name = "pedometer_accumulate") // db의 해당 테이블과 연결
@DynamicUpdate
@IdClass(PedometerId.class)
public class PedometerEntity {
    @Id
    @Column(name = "pedometer_date")
    private Date pedometerDate;
    @Column(name = "mem_id")
    private String memId;
    @Column(name = "pedometer_result")
    private Integer pedometerResult;

    public PedometerDto toDto() {
        return PedometerDto.builder()
                .pedometerDate(pedometerDate)
                .memId(memId)
                .pedometerResult(pedometerResult)
                .build();
    }
}
