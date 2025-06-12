package com.huawei.ibooking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class StudyRoomDO {
    private int id;
    private int state;
    private String buildingNum;
    private String classRoomNum;
    private int startTime = -1;
    private int endTime = -1;
}
