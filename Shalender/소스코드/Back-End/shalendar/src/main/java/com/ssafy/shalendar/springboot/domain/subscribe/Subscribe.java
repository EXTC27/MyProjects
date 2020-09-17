package com.ssafy.shalendar.springboot.domain.subscribe;

import com.ssafy.shalendar.springboot.domain.BaseTimeEntity;
import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Subscribe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fromMember")
    private Member fromMember;

    @OneToOne
    @JoinColumn(name = "toChannel")
    private Channel toChannel;

    @Builder
    public Subscribe(Member fromMember, Channel toChannel) {
        this.fromMember = fromMember;
        this.toChannel = toChannel;
    }

    @Override
    public String toString() {
        return "Subscribe{" +
                "id=" + id +
                ", fromMember=" + fromMember +
                ", toChannel=" + toChannel +
                '}';
    }
}
