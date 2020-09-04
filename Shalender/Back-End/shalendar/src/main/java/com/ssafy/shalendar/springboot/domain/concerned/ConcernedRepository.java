package com.ssafy.shalendar.springboot.domain.concerned;

import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.interest.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcernedRepository extends JpaRepository<Concerned, Long> {

    Integer deleteByChannelAndInterest(Channel channel, Interest interest);
    List<Concerned> findAllByChannelId(String id);
    Concerned findByChannelAndAndInterest(Channel channel, Interest interest);
}
