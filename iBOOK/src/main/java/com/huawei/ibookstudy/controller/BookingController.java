package com.huawei.ibooking.controller;

import com.huawei.ibooking.constant.Const;
import com.huawei.ibooking.model.BookingDo;
import com.huawei.ibooking.model.SeatDo;
import com.huawei.ibooking.model.StudyRoomDO;
import com.huawei.ibooking.service.BookingService;
import com.huawei.ibooking.service.SeatService;
import com.huawei.ibooking.service.StudyRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/v1")
@RestController
public class BookingController {
    @Autowired
    private StudyRoomService studyRoomService;
    @Autowired
    private SeatService seatService;
    @Autowired
    private BookingService bookingService;

    @GetMapping(value = "/studyroom")
    public ResponseEntity<List<StudyRoomDO>> list() {
        final List<StudyRoomDO> rooms = studyRoomService.getStudyRooms();

        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping(value = "/studyroom/{id}")
    public ResponseEntity<StudyRoomDO> getStudyRoomById(@PathVariable("id") int id) {
        Optional<StudyRoomDO> room = studyRoomService.getStudyRoomById(id);

        return room.map(studyRoomDO -> new ResponseEntity<>(studyRoomDO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping(value = "/seat/{roomId}")
    public ResponseEntity<List<SeatDo>> getSeatsByRoomId(@PathVariable("roomId") int roomId) {
        List<SeatDo> seats = seatService.getSeatsByRoomId(roomId);

        return new ResponseEntity<>(seats, HttpStatus.OK);
    }

    @GetMapping(value = "/booking/me")
    public ResponseEntity<List<BookingDo>> getBookingListByStuNum(HttpServletRequest request, HttpServletResponse response) {
        String stuNum = request.getAttribute(Const.SESSION_USERNAME).toString();
        return bookingService.getBookingListByStuNum(stuNum);
    }

    @GetMapping(value = "/booking/seat/{seatId}")
    public ResponseEntity<List<BookingDo>> getBookingListBySeatId(@PathVariable("seatId") int seatId) {
        return bookingService.getBookingListBySeatId(seatId);
    }

    @PostMapping(value = "/booking")
    public ResponseEntity<Map<String, Object>> bookingSeat(@RequestBody BookingDo booking, HttpServletRequest request, HttpServletResponse response) {
        return bookingService.bookingSeat(booking, request, response);
    }

    @PostMapping(value = "/booking/signin")
    public ResponseEntity<Map<String, Object>> signIn(@RequestBody BookingDo booking, HttpServletRequest request, HttpServletResponse response) {
        return bookingService.updateBookingStateSelf(booking.getId(), 1, request, response);
    }

    @PostMapping(value = "/booking/signout")
    public ResponseEntity<Map<String, Object>> signOut(@RequestBody BookingDo booking, HttpServletRequest request, HttpServletResponse response) {
        return bookingService.updateBookingStateSelf(booking.getId(), 4, request, response);
    }

    @PostMapping(value = "/booking/cancel")
    public ResponseEntity<Map<String, Object>> cancel(@RequestBody BookingDo booking, HttpServletRequest request, HttpServletResponse response) {
        return bookingService.updateBookingStateSelf(booking.getId(), 2, request, response);
    }
}
