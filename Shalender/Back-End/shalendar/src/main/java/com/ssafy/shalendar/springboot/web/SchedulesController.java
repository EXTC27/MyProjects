package com.ssafy.shalendar.springboot.web;

import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import com.ssafy.shalendar.springboot.help.BoolResult;
import com.ssafy.shalendar.springboot.help.LongResult;
import com.ssafy.shalendar.springboot.service.SchedulesService;
import com.ssafy.shalendar.springboot.web.dto.schedules.SchedulesResponseDto;
import com.ssafy.shalendar.springboot.web.dto.schedules.SchedulesSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.schedules.SchedulesUpdateRequestDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequiredArgsConstructor
@Slf4j
@CrossOrigin({"*"})
public class SchedulesController {
    private final SchedulesService sService;

    @PostMapping("/makeSchedules")
    public ResponseEntity<Object> makeSchedules(@RequestBody SchedulesSaveRequestDto schedules){
        log.trace("makeSchedules: {}", schedules);
        try{
            LongResult nr = null;
            boolean result = sService.save(schedules);
            if(result){
                Long schNo=sService.findRecentSchNo(schedules.getId());
                nr = new LongResult("makeSchedules", schNo,"SUCCESS");
            }else{
                nr = new LongResult("makeSchedules", -1, "FAIL");
            }
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        }catch (Exception e){
            log.error("makeSchedules", e);
            throw e;
        }
    }

    @PutMapping("/updateSchedules")
    @ApiOperation(value = "일정을 수정한다.", response = BoolResult.class)
    public ResponseEntity<Object> updateSchedules(@RequestBody SchedulesUpdateRequestDto schedules) {
        log.trace("updateSchedules: {}", schedules);
        try {
            boolean result =sService.update(schedules);
            BoolResult br = null;
            if (result) {
                br = new BoolResult(true, "updateSchedules", "SUCCESS");
            } else {
                br = new BoolResult(false, "updateSchedules", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("updateSchedules", e);
            throw e;
        }
    }

    @GetMapping("/getSchedules/{id}/{yymm}")
    public ResponseEntity<Object> getSchedulesByYymm(@PathVariable String id, @PathVariable String yymm) {
        log.trace("getSchedules: {}, {}", id, yymm);
        try {
            List<SchedulesResponseDto> result = sService.getSchedulesYymm(id, yymm);
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("getSchedules", e);
            throw e;
        }
    }


    @GetMapping("findAllSchedules")
    public ResponseEntity<Object> findAllSchedules() {
        log.trace("findAllSchedules");
        try {
            List<Schedules> schedules = sService.searchAll();
            return new ResponseEntity<Object>(schedules, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("findAllSchedules", e);
            throw e;    // spring, tomcat이 받음
        }
    }

    @DeleteMapping("/deleteSchedules/{schNo}")
    @ApiOperation(value = "일정을 삭제한다.", response = BoolResult.class)
    public ResponseEntity<Object> deleteSchedules(@PathVariable Long schNo) {
        log.trace("deleteSchedules: {}", schNo);
        try {
            boolean result = sService.delete(schNo);
            BoolResult br = null;
            if (result) {
                br = new BoolResult(true, "deleteSchedules", "SUCCESS");
            } else {
                br = new BoolResult(false, "deleteSchedules", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("deleteSchedules", e);
            throw e;
        }
    }



}
