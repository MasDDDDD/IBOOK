package com.huawei.ibookstudy.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class BookingDo {
    private int id;
    private int seatId;
    private int studyRoonId;
    private String buildingNum;
    private String classRoonNum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp startTime;

    private int bookingPeriod;
    private String stuNum;
    private int state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp opTime;

    private String detail;
}