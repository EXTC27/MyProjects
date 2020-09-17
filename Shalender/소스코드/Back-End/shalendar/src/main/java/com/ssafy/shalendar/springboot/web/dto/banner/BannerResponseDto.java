package com.ssafy.shalendar.springboot.web.dto.banner;

import com.ssafy.shalendar.springboot.config.ChannelColorConfig;
import com.ssafy.shalendar.springboot.domain.banner.Banner;
import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.channel.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Arrays;

@Getter
@NoArgsConstructor
public class BannerResponseDto extends ChannelColorConfig{
    private Long bn_no;
    private String img;
    private String link;

    public BannerResponseDto(Banner entity) {
        this.bn_no = entity.getBn_no();
        this.img = entity.getImg();
        this.link = entity.getLink();
    }

    public Banner toEntity() {
        return Banner.builder().bn_no(bn_no).img(img).link(link).build();
    }

    @Override
    public String toString() {
        return "BannerResponseDto{" +
                "bn_no=" + bn_no +
                ", img='" + img + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
