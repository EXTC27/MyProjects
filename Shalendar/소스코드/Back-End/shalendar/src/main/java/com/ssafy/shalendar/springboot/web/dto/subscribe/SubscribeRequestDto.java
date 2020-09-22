package com.ssafy.shalendar.springboot.web.dto.subscribe;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SubscribeRequestDto {
    private String fromId;
    private String toChannel;

    @Builder
    public SubscribeRequestDto(String fromId, String toCheannel) {
        this.fromId = fromId;
        this.toChannel = toCheannel;
    }
}
