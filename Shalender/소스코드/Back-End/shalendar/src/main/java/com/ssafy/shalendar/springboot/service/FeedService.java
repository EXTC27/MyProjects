package com.ssafy.shalendar.springboot.service;

import com.ssafy.shalendar.springboot.domain.channel.ChannelRepository;
import com.ssafy.shalendar.springboot.domain.feed.Feed;
import com.ssafy.shalendar.springboot.domain.feed.FeedRepository;

import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import com.ssafy.shalendar.springboot.domain.schedules.SchedulesRepository;
import com.ssafy.shalendar.springboot.web.dto.channel.ChannelResponseDto;
import com.ssafy.shalendar.springboot.web.dto.feed.*;
import com.ssafy.shalendar.springboot.web.dto.subscribe.SubscribeListResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FeedService {

    private static Logger logger = LoggerFactory.getLogger(FeedService.class);
    private final FeedRepository feedRepository;
    private final SchedulesRepository schedulesRepository;
    private final ChannelRepository channelRepositroy;

    @Transactional
    public boolean save(FeedSaveRequestDto requestDto) {
        Long schNo = requestDto.getSchNo();
        try {
            Optional<Schedules> Oschedules = schedulesRepository.findById(schNo);
            Schedules sch = Oschedules.get();
            Feed newFeed = Feed.builder().content(requestDto.getContent()).schedules(sch).img(requestDto.getImg()).id(requestDto.getId()).video(requestDto.getVideo()).build();
            if (newFeed == null) {
                return false;
            }
            feedRepository.save(newFeed);
            return true;
        } catch (RuntimeException e) {
            logger.error("FEED 공유 실패", e);
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<FeedListResponseDto> findAllDesc() {
        return feedRepository.findAllDesc().stream()
                .map(FeedListResponseDto::new)  //.map(posts -> new PostsListResponseDto(posts)) 와 같은 람다식
                .collect(Collectors.toList());
    }

    public List<FeedSubscribeListDto> feedInfoForMember(List<SubscribeListResponseDto> subscribeList, FeedSubscribeRequestDto fsrDto){
        List<FeedListResponseDto> feedList=feedRepository.findAllDesc().stream()
                .map(FeedListResponseDto::new)  //.map(posts -> new PostsListResponseDto(posts)) 와 같은 람다식
                .collect(Collectors.toList());
        List<FeedSubscribeListDto> result =new LinkedList<>();
        int idx=0;
        for(int i=0;i<feedList.size();++i){
            boolean check=false;
            for(int j=0;j<subscribeList.size();++j){
                if(feedList.get(i).getId().equals(subscribeList.get(j).getId())){
                    check=true;
                    break;
                }
            }
            if(!check) {
                feedList.remove(i);
                i--;
            }else{
                if(feedList.get(i).getFeedNo().equals(fsrDto.getLast())){
                    idx=i+1;
                }
            }
        }
        if(idx+fsrDto.getCount()>feedList.size()){
            for (int i = idx; i < feedList.size(); i++) {
                ChannelResponseDto ch= new ChannelResponseDto(channelRepositroy.findChannelById(feedList.get(i).getId()));
                result.add(new FeedSubscribeListDto(feedList.get(i), new SubscribeListResponseDto(ch.getId(),ch.getNickname(),ch.getImg(),ch.getCh_no())));
            }
        }else{
            for (int i = idx; i < idx+fsrDto.getCount(); i++) {
                ChannelResponseDto ch= new ChannelResponseDto(channelRepositroy.findChannelById(feedList.get(i).getId()));
                result.add(new FeedSubscribeListDto(feedList.get(i), new SubscribeListResponseDto(ch.getId(),ch.getNickname(),ch.getImg(),ch.getCh_no())));
            }
        }
        return result;
    }

    public List<FeedSubscribeListDto> feedInfoForChannel(FeedSubscribeRequestDto fsrDto){
        List<FeedListResponseDto> feedList=feedRepository.findAllDesc().stream()
                .map(FeedListResponseDto::new)  //.map(posts -> new PostsListResponseDto(posts)) 와 같은 람다식
                .collect(Collectors.toList());
        List<FeedSubscribeListDto> result =new LinkedList<>();
        int idx=0;
        for(int i=0; i<feedList.size();++i){
            if(!feedList.get(i).getId().equals(fsrDto.getId())){
                feedList.remove(i);
                i--;
            }else{
                if(feedList.get(i).getFeedNo().equals(fsrDto.getLast())){
                    idx=i+1;
                }
            }
        }
        if(idx+fsrDto.getCount()>feedList.size()){
            for (int i = idx; i < feedList.size(); i++) {
                ChannelResponseDto ch= new ChannelResponseDto(channelRepositroy.findChannelById(feedList.get(i).getId()));
                result.add(new FeedSubscribeListDto(feedList.get(i), new SubscribeListResponseDto(ch.getId(),ch.getNickname(),ch.getImg(),ch.getCh_no())));
            }
        }else{
            for (int i = idx; i < idx+fsrDto.getCount(); i++) {
                ChannelResponseDto ch= new ChannelResponseDto(channelRepositroy.findChannelById(feedList.get(i).getId()));
                result.add(new FeedSubscribeListDto(feedList.get(i), new SubscribeListResponseDto(ch.getId(),ch.getNickname(),ch.getImg(),ch.getCh_no())));
            }
        }
        return result;
    }

    @Transactional
    public Boolean updatePath(Long feedNo, String path, int option) {
        try {
            Feed feed = feedRepository.findById(feedNo).get();
            if (option == 1) {
                feed.update(new FeedUpdateRequestDto(feed.getContent(), feed.getSchNo(), path, feed.getId(), feed.getVideo()));
            } else if (option == 2) {
                feed.update(new FeedUpdateRequestDto(feed.getContent(), feed.getSchNo(), feed.getImg(), feed.getId(), path));
            } else if (option == 3) {
                feed.update(new FeedUpdateRequestDto(feed.getContent(), feed.getSchNo(), path, feed.getId(), feed.getVideo()));
            } else if (option == 4) {
                feed.update(new FeedUpdateRequestDto(feed.getContent(), feed.getSchNo(), feed.getImg(), feed.getId(), path));
            }
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean delete(Long id) {
        try {
            Optional<Feed> OFeed = feedRepository.findById(id);
            Feed feed = OFeed.get();
            if (feed == null) {
                return false;
            }
            feedRepository.delete(feed);
            return true;
        } catch (RuntimeException e) {
            logger.error("FEED 삭제 실패", e);
            return false;
        }
    }

    public Long findRecentFeedNo(Long schNo){
        Long feedNo = 0l;
        Schedules schedules = schedulesRepository.findById(schNo).get();
        feedNo = feedRepository.findBySchNoOrderByCreatedDateDesc(schedules);
        return feedNo;
    }
}
