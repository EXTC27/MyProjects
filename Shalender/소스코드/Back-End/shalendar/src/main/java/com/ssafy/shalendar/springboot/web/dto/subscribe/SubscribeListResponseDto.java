package com.ssafy.shalendar.springboot.web.dto.subscribe;

import com.ssafy.shalendar.springboot.config.ChannelColorConfig;
import lombok.Getter;

@Getter
public class SubscribeListResponseDto extends ChannelColorConfig {
    private String id;
    private String nickName;
    private String img;
    private String[] color;

    public SubscribeListResponseDto(String id, String nickName, String img){// }, String color) {
        this.id = id;
        this.nickName = nickName;
        this.img = img;
//        this.color = color;
    }
    public SubscribeListResponseDto(String id, String nickName, String img, Long chNo) {
        this.id = id;
        this.nickName = nickName;
        this.img = img;
        this.color= getColor(chNo.intValue()%19);
    }
}
