package com.ssafy.shalendar.springboot.help;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChannelToSubscriber {
    private String channelId;
    private Integer countSubscriber;

}
