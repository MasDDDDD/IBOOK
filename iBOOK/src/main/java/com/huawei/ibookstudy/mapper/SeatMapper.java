package com.huawei.ibooking.mapper;

import com.huawei.ibooking.model.SeatDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SeatMapper {
    List<SeatDo> getSeatsByRoomId(@Param("roomId") int studyRoomId);

    SeatDo getSeatById(@Param("id") int id);

    int addSeat(@Param("seat") SeatDo seat);

    int updateSeat(@Param("seat") SeatDo seat);

    int deleteSeat(@Param("id") int id);
}
