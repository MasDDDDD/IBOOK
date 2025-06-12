package com.huawei.ibookstudy.controller;

import com.huawei.ibookstudy.constant.Const;
import com.huawei.ibookstudy.model.BookingDo;
import com.huawei.ibookstudy.model.SeatDo;
import com.huawei.ibookstudy.model.StudyRoomDo;
import com.huawei.ibookstudy.service.BookingService;
import com.huawei.ibookstudy.service.SeatService;
import com.huawei.ibookstudy.service.StudyRoomService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/v1")
@RestController
public class BookingController {
    private StudyRoomService studyRoomService;
    private SeatService seatService;
    private BookingService bookingService;
    @Autowired
    public BookingController(StudyRoomService studyRoomService, SeatService seatService, BookingService bookingService) {
        this.studyRoomService = studyRoomService;
        this.seatService = seatService;
        this.bookingService = bookingService;
    }

    @GetMapping(value = "/studyroom")
    public ResponseEntity<List<StudyRoomDo>> list() {
        final List<StudyRoomDo> rooms = studyRoomService.getStudyRooms();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping(value = "/studyroom/{id}")
    public ResponseEntity<StudyRoomDo> getStudyRoomById(@PathVariable("id") int id) {
        Optional<StudyRoomDo> room = studyRoomService.getStudyRoomById(id);
        return room.map(studyRoomDo -> new ResponseEntity<>(studyRoomDo, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping(value = "/seat/{roomId}")
    public ResponseEntity<List<SeatDo>> getSeatsByRoomId(@PathVariable("roomId") int roomId) {
        List<SeatDo> seats = seatService.getSeatsByRoomId(roomId);
        return new ResponseEntity<>(seats, HttpStatus.OK);
    }

    @GetMapping(value = "/booking/me")
    public ResponseEntity<List<BookingDo>> getBookingListByStuNum(HttpServletRequest request, HttpServletResponse response) {
        // HttpServletRequest request, HttpServletResponse response 这两个参数由 Spring MVC 自动注入。
        // 你在方法参数里直接声明，Spring 框架会在收到请求时，把当前请求的 request 和 response 对象自动传入，不需要你手动绑定。
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
