package com.huawei.ibookstudy.dao;

import com.huawei.ibookstudy.mapper.BookingMapper;
import com.huawei.ibookstudy.model.BookingDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingDao {
    @Autowired
    private BookingMapper bookingMapper;

    public BookingDo getBookingById(final int id) { return bookingMapper.getBookingById(id); }

    public List<BookingDo> getAvailableBookingList() { return bookingMapper.getAviailableBookingList();}

    public List<BookingDo> getBookingListByStuNum(final String stuNum) { return bookingMapper.getBookingListByStuNum(stuNum);}

    public List<BookingDo> getBookingListBySeatId(final int seatId) { return bookingMapper.getBookingListBySeatId(seatId);}

    public boolean grabSeat(final BookingDo booking) {
        return bookingMapper.grabSeat(booking) > 0;
    }

    public boolean bookingSeat(final BookingDo booking) {
        return bookingMapper.bookingSeat(booking) > 0;
    }

    public boolean updateBookingState(final int id, final int state, final String detail) {
        return bookingMapper.updateBookingState(id, state, detail) > 0;
    }

    public boolean deleteBooking(final int id) {
        return bookingMapper.deleteBooking(id) > 0;
    }
}
