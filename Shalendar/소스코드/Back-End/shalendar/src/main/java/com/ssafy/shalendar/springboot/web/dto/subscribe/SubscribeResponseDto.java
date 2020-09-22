package com.ssafy.shalendar.springboot.web.dto.subscribe;

import com.ssafy.shalendar.springboot.domain.follow.Follow;
import com.ssafy.shalendar.springboot.domain.subscribe.Subscribe;
import lombok.Getter;

@Getter
public class SubscribeResponseDto {
    private Long id;
    private Long fromMem_no;
    private Long toChannel_no;


    public SubscribeResponseDto(Subscribe entity) {
        this.id = entity.getId();
        this.fromMem_no = entity.getFromMember().getMem_no();
        this.toChannel_no = entity.getToChannel().getCh_no();
    }

}
