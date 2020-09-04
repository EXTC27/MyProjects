package com.ssafy.shalendar.springboot.web.dto.schedules;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SchedulesGetYymmDto {
    private String id;
    private String yymm;

    @Builder
    public SchedulesGetYymmDto(String id, String yymm){
        this.id=id;
        this.yymm=yymm;
    }
}
