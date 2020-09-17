package com.ssafy.shalendar.springboot.web;

import com.ssafy.shalendar.springboot.service.BannerService;
import com.ssafy.shalendar.springboot.web.dto.banner.BannerResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/banner")
@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin({"*"})
public class BannerController {

    private final BannerService bService;

    @GetMapping("/getAllBanners")
    public ResponseEntity<Object> getAllBanners(){
        log.trace("getAllBanners");
        try {
            List<BannerResponseDto> result = bService.getBannerList();
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("getAllBanners", e);
            throw e;
        }
    }
}
