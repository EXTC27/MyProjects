package com.ssafy.shalendar.springboot.web.dto.feed;

import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.ScheduledExecutorService;

@Getter
@NoArgsConstructor
public class FeedUpdateRequestDto {
    private String content;
    private Schedules schedules;
    private String img;
    private String video;
    private String id;

    @Builder
    public FeedUpdateRequestDto(String content, Schedules schedules, String img, String id, String video) {
        this.content = content;
        this.img = img;
        this.schedules = schedules;
        this.id=id;
        this.video=video;
    }
}
