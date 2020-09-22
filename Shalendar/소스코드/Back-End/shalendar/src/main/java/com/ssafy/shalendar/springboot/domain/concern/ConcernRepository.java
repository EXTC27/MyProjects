package com.ssafy.shalendar.springboot.domain.concern;

import com.ssafy.shalendar.springboot.domain.interest.Interest;
import com.ssafy.shalendar.springboot.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcernRepository extends JpaRepository<Concern, Long> {

    Integer deleteByMemberAndInterest(Member member, Interest interest);
    List<Concern> findAllByMember_Id(String id);
    Concern findByMemberAndInterest(Member member, Interest interest);
}
