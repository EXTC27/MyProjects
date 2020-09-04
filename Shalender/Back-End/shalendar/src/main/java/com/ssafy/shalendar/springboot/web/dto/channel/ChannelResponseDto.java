package com.ssafy.shalendar.springboot.web.dto.channel;

import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.channel.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.ssafy.shalendar.springboot.config.ChannelColorConfig;

import java.util.Arrays;

@Getter
@NoArgsConstructor
public class ChannelResponseDto extends ChannelColorConfig{
    private Long ch_no;
    private String id;
    private String pw;
    private String name;
    private String nickname;
    private String img;
    private String link;
    private String poster;
    private String msg;
    private String[] color;
    private Role role;
    private Long searchFrequency;


    public ChannelResponseDto(Channel entity) {
        this.ch_no = entity.getCh_no();
        this.id = entity.getId();
        this.pw = entity.getPw();
        this.name = entity.getName();
        this.nickname = entity.getNickname();
        this.img = entity.getImg();
        this.link = entity.getLink();
        this.msg = entity.getMsg();
        this.poster = entity.getPoster();
        this.role = entity.getRole();
        this.color= getColor(entity.getCh_no().intValue()%19);
        this.searchFrequency = entity.getSearchFrequency();
    }

    public Channel toEntity() {
        return Channel.builder().ch_no(ch_no).id(id).pw(pw).name(name).nickname(nickname).img(img).link(link).msg(msg).poster(poster).role(role).searchFrequency(searchFrequency).build();
    }

    @Override
    public String toString() {
        return "ChannelResponseDto{" +
                "ch_no=" + ch_no +
                ", id='" + id + '\'' +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", img='" + img + '\'' +
                ", link='" + link + '\'' +
                ", poster='" + poster + '\'' +
                ", msg='" + msg + '\'' +
                ", color=" + Arrays.toString(color) +
                ", role=" + role +
                ", searchFrequency=" + searchFrequency +
                '}';
    }
}
