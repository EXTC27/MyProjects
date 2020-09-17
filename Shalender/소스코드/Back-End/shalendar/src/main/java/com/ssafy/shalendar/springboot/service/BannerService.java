package com.ssafy.shalendar.springboot.service;

import com.ssafy.shalendar.springboot.domain.banner.Banner;
import com.ssafy.shalendar.springboot.domain.banner.BannerRepository;
import com.ssafy.shalendar.springboot.domain.interest.Interest;
import com.ssafy.shalendar.springboot.domain.interest.InterestRepository;
import com.ssafy.shalendar.springboot.web.dto.banner.BannerResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BannerService {

    private static Logger logger = LoggerFactory.getLogger(BannerService.class);
    private final BannerRepository bannerRepository;

    public List<BannerResponseDto> getBannerList(){
        List<Banner> banners = bannerRepository.findAll();
        List<BannerResponseDto> result = new ArrayList<>();
        for (Banner banner : banners) {
            result.add(new BannerResponseDto(banner));
        }
        return result;
    }
}
