package com.ssafy.shalendar.springboot.web.dto.channel;

import com.ssafy.shalendar.springboot.config.ChannelColorConfig;
import com.ssafy.shalendar.springboot.domain.channel.Channel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChannelScheduleResponseDto extends ChannelColorConfig {
    private String id;
    private String[] color;

    public ChannelScheduleResponseDto(Channel entity) {
        this.id=entity.getId();
        this.color= getColor(entity.getCh_no().intValue()%19);
    }
}
