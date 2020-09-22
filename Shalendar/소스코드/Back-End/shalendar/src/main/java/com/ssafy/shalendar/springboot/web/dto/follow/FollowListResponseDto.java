package com.ssafy.shalendar.springboot.web.dto.follow;

import lombok.Getter;

@Getter
public class FollowListResponseDto {
    private String id;
    private String nickName;
    private String img;

    public FollowListResponseDto(String id, String nickName, String img) {
        this.id = id;
        this.nickName = nickName;
        this.img = img;
    }
}
