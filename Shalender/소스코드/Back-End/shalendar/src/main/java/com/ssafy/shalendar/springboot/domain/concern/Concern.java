package com.ssafy.shalendar.springboot.domain.concern;

import com.ssafy.shalendar.springboot.domain.BaseTimeEntity;
import com.ssafy.shalendar.springboot.domain.interest.Interest;
import com.ssafy.shalendar.springboot.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Concern extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//시퀀스

    @ManyToOne
    @JoinColumn(name = "memNo")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "interestNo")
    private Interest interest;

    @Builder //해당 클래스의 빌더 패턴 클래스 생성자 상단에 선언시 생성자에 포함된 필드만 빌더에 포함
    public Concern(Member member, Interest interest){
        this.member = member;
        this.interest = interest;
    }

    @Override
    public String toString() {
        return "Concern{" +
                "id=" + id +
                ", member=" + member +
                ", interest=" + interest +
                '}';
    }
}
