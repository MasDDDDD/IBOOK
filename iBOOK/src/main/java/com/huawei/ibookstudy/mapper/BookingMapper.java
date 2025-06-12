package com.huawei.ibookstudy.mapper;

import com.huawei.ibookstudy.model.BookingDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookingMapper {
    List<BookingDo> getAviailableBookingList();
    List<BookingDo> getBookingListByStuNum(@Param("stuNum") String stuNum);
    List<BookingDo> getBookingListBySeatId(@Param("seatId") int seatId);

    BookingDo getBookingById(@Param("id") int seatId);

    int bookingSeat(@Param("booking") BookingDo booking);
    int grabSeat(@Param("booking") BookingDo booking);
    int updateBookingState(@Param("id") int id, @Param("state") int state, @Param("detail") String detail);
    int deleteBooking(@Param("id") int id);
}
