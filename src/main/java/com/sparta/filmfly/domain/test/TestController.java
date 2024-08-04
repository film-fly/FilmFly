package com.sparta.filmfly.domain.test;

import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestController {

    @GetMapping("/1")
    public String test1() {
        log.info("test1");
        return "test2";
    }

    @GetMapping("/2")
    public ResponseEntity<String> test2() {
        log.info("test2");
        return ResponseEntity.ok("이것은 테스트2의 성공인 것이야");
    }

    @GetMapping("/3")
    public ResponseEntity<DataResponseDto<String>> test3() {
        log.info("test3");
        return ResponseUtils.success("테스트3의 성공");
    }
}
