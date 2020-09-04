package com.ssafy.shalendar.springboot.service;

import com.ssafy.shalendar.springboot.domain.interest.Interest;
import com.ssafy.shalendar.springboot.domain.interest.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InterestService {

    private static Logger logger = LoggerFactory.getLogger(InterestService.class);
    private final InterestRepository interestRepository;

    public List<String> getInterestList(){
        List<Interest> interests = interestRepository.findAll();
        List<String> result = new ArrayList<>();
        for (Interest interest : interests){
            result.add(interest.getInterestName());
        }
        return result;
    }
}
