package com.example.youcandoit.entity.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OppositeId {
    private Date certifyDate;
    private Integer groupNumber;
    private String memId;
    private String oppositeId;
}
