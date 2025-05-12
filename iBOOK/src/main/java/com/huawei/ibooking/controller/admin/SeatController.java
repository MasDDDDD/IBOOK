package com.huawei.ibooking.controller.admin;

import com.huawei.ibooking.model.SeatDo;
import com.huawei.ibooking.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/admin")
public class SeatController {
    @Autowired
    private SeatService seatService;

    @PostMapping(value = "/seat")
    public ResponseEntity<Map<String, Object>> addSeat(@RequestBody SeatDo seat) {
        return seatService.addSeat(seat);
    }

    @PostMapping(value = "/seat/state")
    public ResponseEntity<Map<String, Object>> changeState(@RequestBody SeatDo seat) {
        return seatService.changeState(seat);
    }

    @DeleteMapping(value = "/seat")
    public ResponseEntity<Void> delete(@RequestBody SeatDo seat) {
        boolean result = seatService.deleteSeat(seat.getId());
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
