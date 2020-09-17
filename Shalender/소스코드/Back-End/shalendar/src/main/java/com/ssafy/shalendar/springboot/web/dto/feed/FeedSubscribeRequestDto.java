package com.ssafy.shalendar.springboot.web.dto.feed;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedSubscribeRequestDto {
    private Long last;
    private String id;
    private int count;

    @Builder
    public FeedSubscribeRequestDto(Long last, String id, int count){
        this.id=id;
        this.last=last;
        this.count=count;
    }

    @Override
    public String toString() {
        return "FeedSubscribeRequestDto{" +
                "last=" + last +
                ", id='" + id + '\'' +
                ", count=" + count +
                '}';
    }
}
