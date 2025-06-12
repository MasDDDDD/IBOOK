package com.huawei.ibookstudy.controller.admin;

import com.huawei.ibookstudy.model.SeatDo;
import com.huawei.ibookstudy.service.SeatService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("v1/admin")
public class SeatController {
    private SeatService seatService;
    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

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
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
