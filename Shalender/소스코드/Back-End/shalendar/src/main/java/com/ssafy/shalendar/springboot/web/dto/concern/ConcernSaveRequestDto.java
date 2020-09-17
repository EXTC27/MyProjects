package com.ssafy.shalendar.springboot.web.dto.concern;

import com.ssafy.shalendar.springboot.domain.concern.Concern;
import com.ssafy.shalendar.springboot.domain.interest.Interest;
import com.ssafy.shalendar.springboot.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConcernSaveRequestDto {
    private String id;
    private String interest;

    @Builder
    public ConcernSaveRequestDto(String id, String interest){
        this.id = id;
        this.interest = interest;
    }

    @Override
    public String toString() {
        return "ConcernSaveRequestDto{" +
                "id='" + id + '\'' +
                ", interest='" + interest + '\'' +
                '}';
    }
}
