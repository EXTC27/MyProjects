package com.ssafy.shalendar.springboot.web.dto.feed;

import com.ssafy.shalendar.springboot.domain.feed.Feed;
import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import lombok.Getter;

@Getter
public class FeedListResponseDto {
    private Long feedNo;
    private String content;
    private Schedules schedules;
    private String img;
    private String id;
    private String video;

    public FeedListResponseDto(Feed Entity) {
        this.feedNo = Entity.getFeedNo();
        this.content = Entity.getContent();
        this.schedules = Entity.getSchNo();
        this.img=Entity.getImg();
        this.id=Entity.getId();
        this.video=Entity.getVideo();
    }
}
