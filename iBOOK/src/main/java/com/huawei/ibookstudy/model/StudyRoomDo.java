package com.huawei.ibookstudy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class StudyRoomDo {
    private int id;
    private int state;
    private String buildingNum;
    private String classRoomNum;
    private int startTime = -1;
    private int endTime = -1;
}
