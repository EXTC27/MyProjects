package com.ssafy.shalendar.springboot.web.dto.member;

import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.domain.member.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {
    private Long mem_no;
    private String id;
    private String pw;
    private String name;
    private String birth;
    private String nickname;
    private String img;
    private String link;
    private String msg;
    private Role role;

    public MemberResponseDto(Member entity) {
        this.mem_no = entity.getMem_no();
        this.id = entity.getId();
        this.pw = entity.getPw();
        this.name = entity.getName();
        this.birth = entity.getBirth();
        this.nickname = entity.getNickname();
        this.img = entity.getImg();
        this.link = entity.getLink();
        this.msg = entity.getMsg();
        this.role = entity.getRole();
    }

    public Member toEntity() {
        return Member.builder().mem_no(mem_no).id(id).pw(pw).name(name).birth(birth).nickname(nickname).img(img).link(link).msg(msg).role(role).build();
    }

    @Override
    public String toString() {
        return "MemberResponseDto{" +
                "mem_no=" + mem_no +
                ", id='" + id + '\'' +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", birth='" + birth + '\'' +
                ", nickname='" + nickname + '\'' +
                ", img='" + img + '\'' +
                ", link='" + link + '\'' +
                ", msg='" + msg + '\'' +
                ", role=" + role +
                '}';
    }
}
