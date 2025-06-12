package com.huawei.ibookstudy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class SeatDo {
    private int id;
    private int studyRoomId = -1;
    private int state;
}
