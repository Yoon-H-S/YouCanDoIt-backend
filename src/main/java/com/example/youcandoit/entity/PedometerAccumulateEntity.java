package com.example.youcandoit.entity;

import com.example.youcandoit.dto.MemberDto;
import com.example.youcandoit.dto.PedometerAccumulateDto;
import com.example.youcandoit.entity.Id.GroupPersonId;
import com.example.youcandoit.entity.Id.PedometerAccumulateId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity // 이 클래스는 엔티티이다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pedometer_accumulate") // db의 해당 테이블과 연결
@DynamicUpdate
@IdClass(PedometerAccumulateId.class)
public class PedometerAccumulateEntity {
    @Id
    @Column(name = "group_number")
    private Integer groupNumber;
    @Id
    @Column(name = "mem_id")
    private String memId;
    @Column(name = "pedometer_count")
    private Integer pedometerCount;
    @Column(name = "pedoaccu_rank")
    private Integer pedoaccuRank;

    public PedometerAccumulateDto toDto() {
        return PedometerAccumulateDto.builder()
                .groupNumber(groupNumber)
                .memId(memId)
                .pedometerCount(pedometerCount)
                .pedoaccuRank(pedoaccuRank)
                .build();
    }
}
