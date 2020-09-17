package com.ssafy.shalendar.springboot.domain.follow;

import com.ssafy.shalendar.springboot.domain.BaseTimeEntity;
import com.ssafy.shalendar.springboot.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Follow extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//시퀀스

    //중간 테이블 생성됨.
    //fromUser가 toUser를 following함
    //toUser를 fromUser가 follower함.
    @ManyToOne
    @JoinColumn(name = "fromMember")
    private Member fromMember;

    @ManyToOne
    @JoinColumn(name = "toMember")
    private Member toMember;

    @Builder //해당 클래스의 빌더 패턴 클래스 생성자 상단에 선언시 생성자에 포함된 필드만 빌더에 포함
    public Follow(Member fromMember, Member toMember){
        this.fromMember = fromMember;
        this.toMember = toMember;
    }

    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", from=" + fromMember +
                ", to=" + toMember +
                '}';
    }
}

