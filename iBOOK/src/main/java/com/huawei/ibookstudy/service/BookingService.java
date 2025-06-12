package com.huawei.ibooking.service;

import com.huawei.ibooking.model.BookingDo;
import com.huawei.ibooking.model.SeatDo;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface BookingService {
    ResponseEntity<List<BookingDo>> getBookingListByStuNum(final String stuNum);
    ResponseEntity<List<BookingDo>> getBookingListBySeatId(final int seatId);
    ResponseEntity<Map<String, Object>> updateBookingState(final int id, final int state, final String detail);
    ResponseEntity<Map<String, Object>> updateBookingStateSelf(final int id, final int state, HttpServletRequest request, HttpServletResponse response);
    ResponseEntity<Map<String, Object>> bookingSeat(final BookingDo booking, HttpServletRequest request, HttpServletResponse response);
    boolean deleteBooking(final int id);
}
