package com.huawei.ibookstudy.service;

import com.huawei.ibookstudy.model.BookingDo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BookingService {
    ResponseEntity<List<BookingDo>> getBookingListByStuNum(final String stuNum);
    ResponseEntity<List<BookingDo>> getBookingListBySeatId(final int seatId);
    ResponseEntity<Map<String, Object>> updateBookingState(final int it, final int state, final String detail);
    ResponseEntity<Map<String, Object>> updateBookingStateSelf(final int id, final int state, HttpServletRequest request, HttpServletResponse response);
    ResponseEntity<Map<String, Object>> bookingSeat(final BookingDo booking, HttpServletRequest request, HttpServletResponse response);
    boolean deleteBooking(final int id);
}

