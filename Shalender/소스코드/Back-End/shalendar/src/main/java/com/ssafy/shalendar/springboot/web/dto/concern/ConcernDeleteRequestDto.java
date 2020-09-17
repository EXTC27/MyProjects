package com.ssafy.shalendar.springboot.web.dto.concern;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConcernDeleteRequestDto {
    private String id;
    private String interest;

    @Builder
    public ConcernDeleteRequestDto(String id, String interest){
        this.id = id;
        this.interest = interest;
    }

    @Override
    public String toString() {
        return "ConcernDeleteRequestDto{" +
                "id='" + id + '\'' +
                ", interest='" + interest + '\'' +
                '}';
    }
}
