package com.ssafy.shalendar.springboot.web.dto.schedules;

import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
public class SchedulesUpdateRequestDto {
    private String title;
    private String contents;
    private ZonedDateTime startAt;
    private ZonedDateTime endAt;
    private String place;
    private String attendants;
    private String id;
    private Long schNo;


    @Builder
    public SchedulesUpdateRequestDto(Long schNo, String title, String contents, ZonedDateTime startAt, ZonedDateTime endAt, String place, String attendants, String id){
        this.title=title;
        this.contents=contents;
        this.startAt = startAt;
        this.endAt = endAt;
        this.place=place;
        this.attendants=attendants;
        this.id=id;
        this.schNo=schNo;
    }

    public Schedules toEntity(){
        return Schedules.builder().title(title).contents(contents).sdate(startAt).edate(endAt).place(place).attendants(attendants).build();
    }

    @Override
    public String toString() {
        return "SchedulesUpdateRequestDto{" +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", place='" + place + '\'' +
                ", attendants='" + attendants + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
