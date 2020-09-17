package com.ssafy.shalendar.springboot.web.dto.concern;

import com.ssafy.shalendar.springboot.domain.concern.Concern;
import lombok.Getter;

@Getter
public class ConcernResponseDto {
    private Long id;
    private Long mem_no;
    private Long interest_no;


    public ConcernResponseDto(Concern entity) {
        this.id = entity.getId();
        this.mem_no = entity.getMember().getMem_no();
        this.interest_no = entity.getInterest().getInterestNo();
    }
}
