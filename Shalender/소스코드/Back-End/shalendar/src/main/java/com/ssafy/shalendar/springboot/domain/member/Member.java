package com.ssafy.shalendar.springboot.domain.member;

import com.ssafy.shalendar.springboot.domain.BaseTimeEntity;
import com.ssafy.shalendar.springboot.web.dto.member.MemberUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Member extends BaseTimeEntity {
    @Column(nullable = false, columnDefinition = "varchar(45)")
    private String id;

    @Id  //해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //PK의 생성 규칙
    private Long mem_no;
//    @Column(nullable = false, columnDefinition = "varchar(200)")
    private String pw;
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String name;
    private String birth;
//    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String nickname;
    @Column(columnDefinition = "varchar(100)")
    private String img;
    private String link;
    @Column(columnDefinition = "varchar(200)")
    private String msg;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(Long mem_no, String id, String pw, String name, String birth, String nickname, String img,
                String link, String msg, Role role) {
        this.mem_no = mem_no;
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.birth = birth;
        this.nickname = nickname;
        this.img = img;
        this.link = link;
        this.msg = msg;
        this.role = role;
    }

    public void update(MemberUpdateRequestDto member) {
        this.pw = member.getPw();
        this.name = member.getName();
        this.birth = member.getBirth();
        this.nickname = member.getNickname();
        this.img = member.getImg();
        this.link = member.getLink();
        this.msg = member.getMsg();
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", mem_no=" + mem_no +
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