package com.ssafy.shalendar.springboot.web.dto.follow;

import com.ssafy.shalendar.springboot.domain.follow.Follow;
import com.ssafy.shalendar.springboot.domain.member.Member;
import lombok.Getter;

@Getter
public class FollowResponseDto {
    private Long id;
    private Long fromMem_no;
    private Long toMemb_no;

    public FollowResponseDto(Follow entity) {
        this.id = entity.getId();
        this.fromMem_no = entity.getFromMember().getMem_no();
        this.toMemb_no = entity.getToMember().getMem_no();
    }
}
