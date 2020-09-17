package com.ssafy.shalendar.springboot.web.dto.feed;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedSaveRequestDto {
    private String content;
    private Long schNo;
    private String img;
    private String id;
    private String video;

    @Builder
    public FeedSaveRequestDto(String content, Long schNo, String img, String id, String video) {
        this.content = content;
        this.schNo = schNo;
        this.img = img;
        this.id = id;
        this.video=video;
    }
}
