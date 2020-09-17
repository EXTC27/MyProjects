package com.ssafy.shalendar.springboot.web;

import com.ssafy.shalendar.springboot.help.BoolResult;
import com.ssafy.shalendar.springboot.help.NumberResult;
import com.ssafy.shalendar.springboot.service.FollowService;
import com.ssafy.shalendar.springboot.web.dto.follow.FollowListResponseDto;
import com.ssafy.shalendar.springboot.web.dto.follow.FollowRequestDto;
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
public class FollowController {

    private final FollowService fService;

    @PostMapping("/follow")
    public ResponseEntity<Object> follow(@RequestBody FollowRequestDto requestDto) {
        log.trace("follow: {}", requestDto);
        BoolResult br = null;
        try {
            if (requestDto.getFromId() == requestDto.getToId()){
                br = new BoolResult(false, "same ID not allow", "FAIL");
            } else if (fService.isExist(requestDto.getFromId(), requestDto.getToId())) {
                br = new BoolResult(false, "already existing follow", "FAIL");
            }else {
                boolean result = fService.follow(requestDto);
                if (result) {
                    br = new BoolResult(true, "follow", "SUCCESS");
                } else {
                    br = new BoolResult(false, "follow", "FAIL");
                }
            }
        } catch (RuntimeException e){
            log.error("follow", e);
            throw e;
        }
        return new ResponseEntity<Object>(br, HttpStatus.OK);
    }

    @DeleteMapping("/unFollow")
    public ResponseEntity<Object> unfollow(@RequestBody FollowRequestDto requestDto) {
        log.trace("unfollow: {}", requestDto);
        BoolResult br = null;
        try {
            if (requestDto.getFromId() == requestDto.getToId()) {
                br = new BoolResult(false, "same ID is not allow", "FAIL");
            } else if (!fService.isExist(requestDto.getFromId(), requestDto.getToId())){
                br = new BoolResult(false, "nothing to unfollow", "FAIL");
            } else {
                boolean result = fService.unfollow(requestDto);
                if (result) {
                    br = new BoolResult(true, "follow", "SUCCESS");
                } else {
                    br = new BoolResult(false, "follow", "FAIL");
                }
            }
        } catch (RuntimeException e){
            log.error("follow", e);
            throw e;
        }
        return new ResponseEntity<Object>(br, HttpStatus.OK);
    }

    @GetMapping("/getFollowList/{id}")
    public ResponseEntity<Object> getFollowList(@PathVariable String id) {
        log.trace("getFollowList: {}", id);
        try {
            List<FollowListResponseDto> followList = fService.findAllMyFollow(id);
            if (followList == null) {
                return new ResponseEntity<Object>("There isn't ID you provide", HttpStatus.OK);
            }
            return new ResponseEntity<Object>(followList, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("getFollowList", e);
            throw e;    // spring, tomcat이 받음
        }
    }

    @GetMapping("/getFollowerList/{id}")
    public ResponseEntity<Object> getFollowerList(@PathVariable String id){
        log.trace("getFollowerList: {}", id);
        try {
            List<FollowListResponseDto> followerList = fService.findAllMyFollower(id);
            if (followerList == null) {
                return new ResponseEntity<Object>("There isn't ID you provide", HttpStatus.OK);
            }
            return new ResponseEntity<Object>(followerList, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("getFollowerList", e);
            throw e;
        }
    }

    @GetMapping("/getCountOfMyFollow/{id}")
    public ResponseEntity<Object> getCountOfMyFollow(@PathVariable String id){
        log.trace("getCountOfMyFollow: {}", id);
        NumberResult nr = null;
        try {
            Integer count = fService.getCountOfMyFollow(id);
            if (count == -1){
                return new ResponseEntity<Object>("There isn't ID you provide", HttpStatus.OK);
            }
            nr = new NumberResult("getCountOfMyFollow", count, "SUCCESS");
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("getCountOfMyFollow", e);
            throw e;
        }
    }

    @GetMapping("/getCountOfMyFollower/{id}")
    public ResponseEntity<Object> getCountOfMyFollower(@PathVariable String id){
        log.trace("getCountOfMyFollower: {}", id);
        NumberResult nr = null;
        try {
            Integer count = fService.getCountOfMyFollower(id);
            if (count == -1){
                return new ResponseEntity<Object>("There isn't ID you provide", HttpStatus.OK);
            }
            nr = new NumberResult("getCountOfMyFollower", count, "SUCCESS");
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("getCountOfMyFollower", e);
            throw e;
        }
    }

    @GetMapping("/isFollowing/{fromId}/{toId}")
    public ResponseEntity<Object> isSubscribed(@PathVariable String fromId, @PathVariable String toId){
        log.trace("isFollowing: {}, {}", fromId, toId);
        BoolResult br = null;
        try {
            if (fromId == toId){
                br = new BoolResult(false, "same ID is not allow", "FAIL");
            } else {
                Boolean result = fService.isExist(fromId, toId);
                if (result) {
                    br = new BoolResult(true, "isFollowing", "SUCCESS");
                } else {
                    br = new BoolResult(false, "isFollowing", "FAIL");
                }
            }
        } catch (RuntimeException e){
            log.error("isFollowing", e);
            throw e;
        }
        return new ResponseEntity<Object>(br, HttpStatus.OK);
    }
}
