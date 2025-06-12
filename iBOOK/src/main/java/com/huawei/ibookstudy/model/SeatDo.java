package com.huawei.ibooking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SeatDo {
    private int id;
    private int studyRoomId = -1;
    private int state;
}
