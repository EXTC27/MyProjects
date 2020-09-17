package com.ssafy.shalendar.springboot.web;

import com.ssafy.shalendar.springboot.help.BoolResult;
import com.ssafy.shalendar.springboot.help.NumberResult;
import com.ssafy.shalendar.springboot.service.ChannelService;
import com.ssafy.shalendar.springboot.service.SubscribeService;
import com.ssafy.shalendar.springboot.web.dto.subscribe.SubscribeListResponseDto;
import com.ssafy.shalendar.springboot.web.dto.subscribe.SubscribeRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/member")
@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin({"*"})
public class SubscribeController {

    private final SubscribeService sService;
    private final ChannelService cService;

    @PostMapping("/subscribe")
    public ResponseEntity<Object> subscribe(@RequestBody SubscribeRequestDto requestDto) {
        log.trace("subscribe: {}", requestDto);
        BoolResult br = null;
        try {
            if (requestDto.getFromId() == requestDto.getToChannel()){
                br = new BoolResult(false, "same ID is not allow", "FAIL");
            } else if (sService.isExist(requestDto.getFromId(), requestDto.getToChannel())){
                br = new BoolResult(false, "already existing subscribing", "FAIL");
            } else {
                boolean result = sService.subscribe(requestDto);
                if (result) {
                    br = new BoolResult(true, "subscribe", "SUCCESS");
                } else {
                    br = new BoolResult(false, "subscribe", "FAIL");
                }
            }
        } catch (RuntimeException e){
            log.error("subscribe", e);
            throw e;
        }
        return new ResponseEntity<Object>(br, HttpStatus.OK);
    }

    @DeleteMapping("/unSubscribe")
    public ResponseEntity<Object> unSubscribe(@RequestBody SubscribeRequestDto requestDto) {
        log.trace("unSubscribe: {}", requestDto);
        BoolResult br = null;
        try {
            if (requestDto.getFromId() == requestDto.getToChannel()) {
                br = new BoolResult(false, "same ID is not allow", "FAIL");
            } else if (!sService.isExist(requestDto.getFromId(), requestDto.getToChannel())){
                br = new BoolResult(false, "nothing to unsubscribe", "FAIL");
            } else {
                boolean result = sService.unSubscribe(requestDto);
                if (result) {
                    br = new BoolResult(true, "subscribe", "SUCCESS");
                } else {
                    br = new BoolResult(false, "subscribe", "FAIL");
                }
            }
        } catch (RuntimeException e){
            log.error("subscribe", e);
            throw e;
        }
        return new ResponseEntity<Object>(br, HttpStatus.OK);
    }

    @GetMapping("/getSubscribeList/{id}")
    public ResponseEntity<Object> getSubscribeList(@PathVariable String id) {
        log.trace("getSubscribeList: {}", id);
        try {
            List<SubscribeListResponseDto> subscribeList = sService.findAllSubscribe(id);
            if (subscribeList == null) {
                return new ResponseEntity<Object>("There isn't ID you provide", HttpStatus.OK);
            }
            return new ResponseEntity<Object>(subscribeList, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("getSubscribeList", e);
            throw e;    // spring, tomcat이 받음
        }
    }

    @GetMapping("/getSubscriberList/{id}")
    public ResponseEntity<Object> getSubscriberList(@PathVariable String id){
        log.trace("getSubscriberList: {}", id);
        try {
            List<SubscribeListResponseDto> subscriberList = sService.findAllSubscriber(id);
            if (subscriberList == null) {
                return new ResponseEntity<Object>("There isn't ID you provide", HttpStatus.OK);
            }
            return new ResponseEntity<Object>(subscriberList, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("getSubscriberList", e);
            throw e;
        }
    }

    @GetMapping("/getCountOfMySubscribe/{id}")
    public ResponseEntity<Object> getCountOfMySubscribe(@PathVariable String id){
        log.trace("getCountOfMySubscribe: {}", id);
        NumberResult nr = null;
        try {
            Integer count = sService.getCountOfMySubscribe(id);
            if (count == -1){
                return new ResponseEntity<Object>("There isn't ID you provide", HttpStatus.OK);
            }
            nr = new NumberResult("getCountOfMySubscribe", count, "SUCCESS");
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("getCountOfMySubscribe", e);
            throw e;
        }
    }

    @GetMapping("/getCountOfMySubscriber/{id}")
    public ResponseEntity<Object> getCountOfMySubscriber(@PathVariable String id){
        log.trace("getCountOfMySubscriber: {}", id);
        NumberResult nr = null;
        try {
            Integer count = sService.getCountOfMySubscriber(id);
            if (count == -1){
                return new ResponseEntity<Object>("There isn't ID you provide", HttpStatus.OK);
            }
            nr = new NumberResult("getCountOfMySubscriber", count, "SUCCESS");
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("getCountOfMySubscriber", e);
            throw e;
        }
    }

    @GetMapping("/isSubscribing/{fromId}/{toChannel}")
    public ResponseEntity<Object> isSubscribing(@PathVariable String fromId, @PathVariable String toChannel){
        log.trace("isSubscribing: {}, {}", fromId, toChannel);
        BoolResult br = null;
        try {
            if (fromId == toChannel){
                br = new BoolResult(false, "same ID is not allow", "FAIL");
            } else {
                Boolean result = sService.isExist(fromId, toChannel);
                if (result) {
                    br = new BoolResult(true, "isSubscribing", "SUCCESS");
                } else {
                    br = new BoolResult(false, "isSubscribing", "FAIL");
                }
            }
        } catch (RuntimeException e){
            log.error("isSubscribing", e);
            throw e;
        }
        return new ResponseEntity<Object>(br, HttpStatus.OK);
    }
}
