package com.example.youcandoit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyRankDto implements Comparable<MyRankDto> {
    private String groupName;
    private String groupSubject;
    private String groupImage;
    private int subDate;
    private int ranking;

    // 사용자 정의 정렬을 위해 override
    @Override
    public int compareTo(MyRankDto o) {
        if(o.subDate < subDate) {
            return 1;
        } else if(o.subDate > subDate) {
            return -1;
        }
        return 0;
    }
}
