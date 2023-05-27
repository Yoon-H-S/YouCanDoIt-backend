package com.example.youcandoit.entity;

import com.example.youcandoit.dto.GodlifeGoalDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "friend") // db의 해당 테이블과 연결
@DynamicUpdate
public class GodlifeGoalEntity {
    @Id
    @Column(name = "mem_id")
    private String memId;
    @Column(name = "goal_pedometer")
    private Integer goalPedometer;

    public GodlifeGoalDto toDto() {
        return GodlifeGoalDto.builder()
                .memId(memId)
                .goalPedometer(goalPedometer)
                .build();
    }
}
