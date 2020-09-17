package com.ssafy.shalendar.springboot.web.dto.feed;

import com.ssafy.shalendar.springboot.domain.feed.Feed;
import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import com.ssafy.shalendar.springboot.web.dto.schedules.SchedulesResponseDto;
import com.ssafy.shalendar.springboot.web.dto.subscribe.SubscribeListResponseDto;
import com.ssafy.shalendar.springboot.web.dto.subscribe.SubscribeResponseDto;
import lombok.Getter;

@Getter
public class FeedSubscribeListDto {
    private Long feedNo;
    private String content;
    private SchedulesResponseDto schedules;
    private String img;
    private String video;
    private SubscribeListResponseDto srDto;

    public FeedSubscribeListDto(FeedListResponseDto Entity, SubscribeListResponseDto srDto) {
        this.feedNo = Entity.getFeedNo();
        this.content = Entity.getContent();
        this.schedules = new SchedulesResponseDto(Entity.getSchedules());
        this.img=Entity.getImg();
        this.video=Entity.getVideo();
        this.srDto=srDto;
    }
}
