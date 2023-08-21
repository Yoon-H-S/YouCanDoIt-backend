package com.example.youcandoit.entity;


import com.example.youcandoit.dto.StickerDto;
import com.example.youcandoit.entity.Id.StickerId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Date;

@Entity // 이 클래스는 엔티티이다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sticker") // db의 해당 테이블과 연결
@DynamicInsert
@DynamicUpdate
@IdClass(StickerId.class)
public class StickerEntity {
    @Id
    @Column(name = "sticker_date")
    private Date stickerDate;
    @Id
    @Column(name = "mem_id")
    private String memId;
    @Column(name = "total_count")
    private Integer totalCount;
    @Column(name = "success_count")
    private Integer successCount;

    public StickerDto toDto() {
        return StickerDto.builder()
                .stickerDate(stickerDate)
                .memId(memId)
                .totalCount(totalCount)
                .successCount(successCount)
                .build();
    }
}
