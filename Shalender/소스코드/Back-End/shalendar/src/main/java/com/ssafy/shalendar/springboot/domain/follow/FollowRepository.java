package com.ssafy.shalendar.springboot.domain.follow;

import com.ssafy.shalendar.springboot.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    void deleteByFromMemberAndToMember(Member fromMember, Member toMember);

    List<Follow> findAllByFromMember(Member fromMember);
    List<Follow> findAllByToMember(Member toMember);
    Integer countAllByFromMember(Member fromMember);
    Integer countAllByToMember(Member toMember);
    boolean existsByFromMemberAndToMember(Member fromMember, Member toMember);


//    @Query("select f from Follow f where f.fromMember =: fromMember")
//    List<Follow> followList(Member fromMember);

}
