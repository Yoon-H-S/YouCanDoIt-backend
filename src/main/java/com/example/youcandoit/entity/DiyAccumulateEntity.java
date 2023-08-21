package com.example.youcandoit.entity;


import com.example.youcandoit.dto.DiyAccumulateDto;
import com.example.youcandoit.entity.Id.DiyAccumulateId;
import jakarta.persistence.*;
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
@Table(name = "diy_accumulate") // db의 해당 테이블과 연결
@DynamicInsert
@DynamicUpdate
@IdClass(DiyAccumulateId.class)
public class DiyAccumulateEntity {
    @Id
    @Column(name = "group_number")
    private Integer groupNumber;
    @Id
    @Column(name = "mem_id")
    private String memId;
    @Column(name = "certify_count")
    private Integer certifyCount;
    @Column(name = "diyaccu_rank")
    private Integer diyaccuRank;

    public DiyAccumulateDto toDto() {
        return DiyAccumulateDto.builder()
                .groupNumber(groupNumber)
                .memId(memId)
                .certifyCount(certifyCount)
                .diyaccuRank(diyaccuRank)
                .build();
    }
}
