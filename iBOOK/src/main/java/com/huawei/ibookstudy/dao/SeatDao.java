package com.huawei.ibookstudy.dao;

import com.huawei.ibookstudy.mapper.SeatMapper;
import com.huawei.ibookstudy.model.SeatDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SeatDao {
    @Autowired
    private SeatMapper seatMapper;

    public List<SeatDo> getSeatsByRoomId(final int roomId) {
        return seatMapper.getSeatsByRoomId(roomId);
    }

    public SeatDo getSeatById(final int id) {
        return seatMapper.getSeatById(id);
    }

    public boolean addSeat(final SeatDo seat) {
        return seatMapper.addSeat(seat) > 0;
    }

    public boolean updateSeat(final SeatDo seat) {
        return seatMapper.updateSeat(seat) > 0;
    }

    public boolean deleteSeat(final int id) {
        return seatMapper.deleteSeat(id) > 0;
    }
}
