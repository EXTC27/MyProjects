package com.ssafy.shalendar.springboot.web.dto.follow;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FollowRequestDto {
    private String fromId;
    private String toId;

    @Builder
    public FollowRequestDto(String fromId, String toId) {
        this.fromId = fromId;
        this.toId = toId;
    }

}
