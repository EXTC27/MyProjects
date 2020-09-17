package com.ssafy.shalendar.springboot.domain.feed;

import com.ssafy.shalendar.springboot.domain.BaseTimeEntity;
import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import com.ssafy.shalendar.springboot.web.dto.feed.FeedUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Feed extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedNo;

    @Column(length = 1000)
    private String content;
    private String img;
    private String id;
    private String video;

    @ManyToOne
    @JoinColumn(name = "schNo")
    private Schedules schNo;


    @Builder
    public Feed(String content, Schedules schedules, String img, String id, String video) {
        this.content = content;
        this.schNo = schedules;
        this.img=img;
        this.id=id;
        this.video=video;
    }

    public void update(FeedUpdateRequestDto feed) {
        this.content = feed.getContent();
        this.schNo = feed.getSchedules();
        this.img = feed.getImg();
        this.id = feed.getId();
        this.video = feed.getVideo();
    }
    @Override
    public String toString() {
        return "Feed{" +
                "feedNo=" + feedNo +
                ", content='" + content + '\'' +
                ", img='" + img + '\'' +
                ", id='" + id + '\'' +
                ", video='" + video + '\'' +
                ", schNo=" + schNo +
                '}';
    }
}