package com.ssafy.shalendar.springboot.domain.channel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Channel findChannelById(String id);
//    @Modifying  // update, delete Query 시 @Modifying 어노테이션 추가
//    @Query(value = "UPDATE Channel c SET c.pw = :#{#channel.pw}, c.nickname = :#{#channel.nickname}, c.img = :#{#channel.img}, c.link = :#{#channel.link}, c.msg = :#{#channel.msg} WHERE c.id = :#{#channel.id}", nativeQuery = false)
//    Boolean update(@Param("channel") Channel channel);
    boolean existsById(String id);
    boolean existsByNickname(String nickname);
    @Query("select c from Channel c where lower(c.nickname) like lower(concat('%', :nickname, '%'))")
    List<Channel> findAllByNicknameLikeOrderBySearchFrequencyDesc(@Param("nickname") String nickname);
    Channel findChannelByIdAndPw(@Param("id") String id, @Param("pw") String pw);
    boolean existsByIdAndPw(@Param("id") String id, @Param("pw") String pw);
}