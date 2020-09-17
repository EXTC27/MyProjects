package com.ssafy.shalendar.springboot.service;

import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.channel.ChannelRepository;
import com.ssafy.shalendar.springboot.domain.feed.FeedRepository;
import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.domain.member.MemberRepository;
import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import com.ssafy.shalendar.springboot.domain.schedules.SchedulesRepository;

import com.ssafy.shalendar.springboot.web.dto.schedules.SchedulesResponseDto;
import com.ssafy.shalendar.springboot.web.dto.schedules.SchedulesSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.schedules.SchedulesUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SchedulesService {
    private static Logger logger = LoggerFactory.getLogger(SchedulesService.class);
    private final SchedulesRepository schedulesRepository;
    private final MemberRepository memberRepository;
    private final ChannelRepository channelRepositroy;
    private final FeedRepository feedRepository;

    @Transactional
    public Boolean save(SchedulesSaveRequestDto requestDto){
        try {
            Member member = memberRepository.findMemberById(requestDto.getId());
            Channel channel = channelRepositroy.findChannelById(requestDto.getId());
            requestDto.setChannel(channel);
            requestDto.setMember(member);
            schedulesRepository.save(requestDto.toEntity());
            return true;
        }catch (RuntimeException e){
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public Boolean update(SchedulesUpdateRequestDto requestDto) {
        try {
            Schedules sch=schedulesRepository.findById(requestDto.getSchNo()).orElseThrow(() -> new IllegalArgumentException("해당 일정이 없습니다. "));
            sch.update(requestDto.toEntity());
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public List<SchedulesResponseDto> getSchedulesYymm(String id, String yymm) {
        List<Schedules> listSchedules;
        List<SchedulesResponseDto> listResponse=new LinkedList<>();
        Member member = memberRepository.findMemberById(id);
        Channel channel= channelRepositroy.findChannelById(id);
        if(member!=null){
            listSchedules=schedulesRepository.findByMemNoAndSdateOrEdate(member, yymm);
        }else{
            listSchedules=schedulesRepository.findByChNoAndSdateOrEdate(channel, yymm);
        }
        for(Schedules sc : listSchedules){
            SchedulesResponseDto schedulesResponseDto=new SchedulesResponseDto(sc);
            listResponse.add(schedulesResponseDto);
        }
        return listResponse;
    }

    public List<Schedules> searchAll() {
        List<Schedules> result = schedulesRepository.findAll();
        return result;
    }

    public Long findRecentSchNo(String id){
        Long schNo=0l;
        Member member = memberRepository.findMemberById(id);
        Channel channel= channelRepositroy.findChannelById(id);
        if(member!=null){
            schNo=schedulesRepository.findByMemNoOrderByCreatedDateDesc(member);
        }else{
            schNo=schedulesRepository.findByChNoOrderByCreatedDateDesc(channel);
        }
        return schNo;
    }

    @Transactional
    public Boolean delete(Long schNo) {
        Schedules sch=null;
        try {
            sch = schedulesRepository.findById(schNo).orElseThrow(() -> new IllegalArgumentException("해당 일정이 없습니다. "));
            if (sch == null) {
                throw new RuntimeException("해당 일정이 없습니다.");
            }
            feedRepository.deleteBySchNo(sch);
            schedulesRepository.delete(sch);
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }
}
