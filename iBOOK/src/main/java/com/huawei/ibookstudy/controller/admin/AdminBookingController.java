package com.huawei.ibookstudy.controller.admin;

import com.huawei.ibookstudy.model.BookingDo;
import com.huawei.ibookstudy.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("v1/admin") // 给整个控制器加了个前缀，后面所有方法的路径都会以 v1/admin 开头。
@RestController  //声明这是一个RESTful 控制器，所有方法返回的都是 JSON 数据
public class AdminBookingController {
    private BookingService bookingService;
    @Autowired
    public AdminBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(value = "/booking/{stuNum}") // 这对应于GET请求，完整路径是 /v1/admin/booking/{stuNum}。{stuNum} 是路径参数。
    public ResponseEntity<List<BookingDo>> getBookingListByStuNum(@PathVariable("stuNum") String stuNum) {
        // 路径参数绑定。@PathVariable("stuNum")：把 URL 路径里的 {stuNum} 绑定到方法参数 stuNum 上
        return bookingService.getBookingListByStuNum(stuNum);
    }

    @PostMapping(value = "/booking/state")
    public ResponseEntity<Map<String, Object>> updateBookingState(@RequestBody BookingDo booking) {
        // 请求体绑定。@RequestBody BookingDo booking：接收请求体中的 JSON 数据，并自动反序列化为 Java 对象 BookingDo。
        return bookingService.updateBookingState(booking.getId(), booking.getState(), booking.getDetail());
    }

    @DeleteMapping(value = "/booking")
    public ResponseEntity<Void> deleteBooking(@RequestBody BookingDo booking) {
        boolean result = bookingService.deleteBooking(booking.getId());
        return new ResponseEntity<>(result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
