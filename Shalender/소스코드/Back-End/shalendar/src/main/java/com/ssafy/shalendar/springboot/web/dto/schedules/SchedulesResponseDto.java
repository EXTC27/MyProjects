package com.ssafy.shalendar.springboot.web.dto.schedules;

import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import com.ssafy.shalendar.springboot.web.dto.channel.ChannelScheduleResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class SchedulesResponseDto {
    private String title;
    private String contents;
    private ZonedDateTime startAt;
    private ZonedDateTime endAt;
    private String place;
    private String attendants;
    private boolean isOneDay;
    private Long schNo;
    private ChannelScheduleResponseDto csrDto;

    public SchedulesResponseDto(Schedules entity){
        this.title=entity.getTitle();
        this.contents=entity.getContents();
        this.place=entity.getPlace();
        this.attendants=entity.getAttendants();
        this.startAt=entity.getSdate();
        this.endAt=entity.getEdate();
        this.schNo=entity.getSchNo();
        if(ChronoUnit.HOURS.between(entity.getSdate(), entity.getEdate())<5)
            this.isOneDay=true;
        else
            this.isOneDay=false;
        if(entity.getChNo()!=null)
            this.csrDto=new ChannelScheduleResponseDto(entity.getChNo());
    }

    @Override
    public String toString() {
        return "SchedulesResponseDto{" +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", place='" + place + '\'' +
                ", attendants='" + attendants + '\'' +
                ", isOneDay=" + isOneDay +
                ", schNo=" + schNo +
                ", csrDto=" + csrDto +
                '}';
    }
}
