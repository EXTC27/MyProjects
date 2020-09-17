package com.ssafy.shalendar.springboot.web.dto.channel;

import com.ssafy.shalendar.springboot.config.ChannelColorConfig;
import com.ssafy.shalendar.springboot.domain.channel.Channel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChannelSearchedResponseDto extends ChannelColorConfig{
    private Long ch_no;
    private String id;
    private String nickname;
    private String img;
    private String poster;
    private List<String> interests;

    public ChannelSearchedResponseDto(Channel entity, List<String> interests) {
        this.ch_no = entity.getCh_no();
        this.id = entity.getId();
        this.nickname = entity.getNickname();
        this.img = entity.getImg();
        this.poster = entity.getPoster();
        this.interests = interests;
    }

    public Channel toEntity() {
        return Channel.builder().ch_no(ch_no).id(id).nickname(nickname).img(img).poster(poster).build();
    }

    @Override
    public String toString() {
        return "ChannelSearchedResponseDto{" +
                "ch_no=" + ch_no +
                ", id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", img='" + img + '\'' +
                ", poster='" + poster + '\'' +
                ", interests=" + interests +
                '}';
    }
}
