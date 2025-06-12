package com.huawei.ibooking.service;

import com.huawei.ibooking.model.SeatDo;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface SeatService {
    List<SeatDo> getSeatsByRoomId(final int roomId);
    ResponseEntity<Map<String, Object>> changeState(final SeatDo seat);
    ResponseEntity<Map<String, Object>> addSeat(final SeatDo seat);
    boolean deleteSeat(final int id);
}
