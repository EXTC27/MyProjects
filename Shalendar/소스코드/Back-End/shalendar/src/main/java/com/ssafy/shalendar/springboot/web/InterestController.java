package com.ssafy.shalendar.springboot.web;

import com.ssafy.shalendar.springboot.service.InterestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/interest")
@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin({"*"})
public class InterestController {
    //어느 컨트롤러에서나 @LoginUser 를 선언하면 세션 정보를 가져올 수 있다

    private final InterestService iService;

    @GetMapping("getAllInterests")
    public ResponseEntity<Object> getAllInterests() {
        log.debug("getAllInterests");
        try {
            List<String> interests = iService.getInterestList();
            Map<String, Boolean> result = new HashMap<>();
            for (String interest : interests) {
                result.put(interest, false);
            }
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("getAllInterests", e);
            throw e;    // spring, tomcat이 받음
        }
    }
}
