package com.huawei.ibooking.controller.admin;

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

import java.util.List;
import java.util.Map;

@RequestMapping("/v1/admin")
@RestController
public class AdminBookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping(value = "/booking/{stuNum}")
    public ResponseEntity<List<BookingDo>> getBookingListByStuNum(@PathVariable("stuNum") String stuNum) {
        return bookingService.getBookingListByStuNum(stuNum);
    }

    @PostMapping(value = "/booking/state")
    public ResponseEntity<Map<String, Object>> updateBookingState(@RequestBody BookingDo booking) {
        return bookingService.updateBookingState(booking.getId(), booking.getState(), booking.getDetail());
    }

    @DeleteMapping(value = "/booking")
    public ResponseEntity<Void> deleteBooking(@RequestBody BookingDo booking) {
        boolean result = bookingService.deleteBooking(booking.getId());
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
