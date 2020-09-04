package com.ssafy.shalendar.springboot.domain.subscribe;

import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    void deleteByFromMemberAndToChannel(Member fromMember, Channel toChannel);

    // 특정 채널의 구독정보
    List<Subscribe> findAllByToChannel(Channel toChannel);
    // 특정 멤버의 구독정보
    List<Subscribe> findAllByFromMember(Member fromMember);
    // 특정 멤버가 구독한 채널 수
    Integer countAllByFromMember(Member fromMember);
    // 특정 채널을 구독한 회원 수
    Integer countAllByToChannel(Channel toChannel);
    boolean existsByFromMemberAndToChannel(Member fromMember, Channel toChannel);

}
