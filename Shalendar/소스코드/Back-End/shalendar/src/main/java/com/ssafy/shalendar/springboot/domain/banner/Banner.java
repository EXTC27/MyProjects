package com.ssafy.shalendar.springboot.domain.banner;

import com.ssafy.shalendar.springboot.domain.BaseTimeEntity;
import com.ssafy.shalendar.springboot.web.dto.channel.ChannelUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Banner extends BaseTimeEntity {
    @Id  //해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //PK의 생성 규칙
    private Long bn_no;
    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String img;
    @Column(nullable = true, columnDefinition = "varchar(200)")
    private String link;

    @Builder
    public Banner(Long bn_no, String img, String link) {
        this.bn_no = bn_no;
        this.img = img;
        this.link = link;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "bn_no=" + bn_no +
                ", img='" + img + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}