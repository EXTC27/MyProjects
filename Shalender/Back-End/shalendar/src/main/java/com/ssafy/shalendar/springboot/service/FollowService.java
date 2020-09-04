package com.ssafy.shalendar.springboot.service;

import com.ssafy.shalendar.springboot.domain.follow.Follow;
import com.ssafy.shalendar.springboot.domain.follow.FollowRepository;
import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.domain.member.MemberRepository;
import com.ssafy.shalendar.springboot.web.dto.follow.FollowListResponseDto;
import com.ssafy.shalendar.springboot.web.dto.follow.FollowRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowService {

    private static Logger logger = LoggerFactory.getLogger(FollowService.class);

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Transactional
    public boolean follow(FollowRequestDto requestDto) {
        String fromId = requestDto.getFromId();
        String toId = requestDto.getToId();
        try {
            Member fromMember = memberRepository.findMemberById(fromId);
            Member toMember = memberRepository.findMemberById(toId);
            if (fromMember == null || toMember == null){
                return false;
            }
            Follow newFollow = Follow.builder().fromMember(fromMember).toMember(toMember).build();
            followRepository.save(newFollow);
            return true;
        } catch (RuntimeException e){
            logger.error("팔로우 실패", e);
            return false;
        }
    }

    @Transactional
    public boolean unfollow(FollowRequestDto requestDto) {
        String fromId = requestDto.getFromId();
        String toId = requestDto.getToId();
        try {
            Member fromMember = memberRepository.findMemberById(fromId);
            Member toMember = memberRepository.findMemberById(toId);
            if (fromMember == null || toMember == null){
                return false;
            }
            followRepository.deleteByFromMemberAndToMember(fromMember, toMember);
            return true;
        } catch (RuntimeException e) {
            logger.error("언팔로우 실패", e);
            return false;
        }
    }

    public List<FollowListResponseDto> findAllMyFollow(String fromId) {
        Member fromMember = memberRepository.findMemberById(fromId);
        if (fromMember == null){
            return null;
        }
        List<Follow> followList = followRepository.findAllByFromMember(fromMember);

        List<FollowListResponseDto> members = new ArrayList<>();
        for (Follow follow : followList) {
            String id = follow.getToMember().getId();
            Member member = memberRepository.findMemberById(id);
            FollowListResponseDto dto = new FollowListResponseDto(member.getId(), member.getNickname(), member.getImg());
            members.add(dto);
        }

        return members;
    }

    public List<FollowListResponseDto> findAllMyFollower(String toId) {
        Member toMember = memberRepository.findMemberById(toId);
        if (toMember == null){
            return null;
        }
        List<Follow> followerList = followRepository.findAllByToMember(toMember);

        List<FollowListResponseDto> members = new ArrayList<>();
        for (Follow follow : followerList){
            String id = follow.getFromMember().getId();
            Member member = memberRepository.findMemberById(id);
            FollowListResponseDto dto = new FollowListResponseDto(member.getId(), member.getNickname(), member.getImg());
            members.add(dto);
        }
        return members;
    }

    public Integer getCountOfMyFollow(String fromId){
        Member fromMember = memberRepository.findMemberById(fromId);
        if (fromMember == null){
            return -1;
        }
        return followRepository.countAllByFromMember(fromMember);
    }

    public Integer getCountOfMyFollower(String toId){
        Member toMember = memberRepository.findMemberById(toId);
        if (toMember == null){
            return -1;
        }
        return followRepository.countAllByToMember(toMember);
    }

    public Boolean isExist(String fromId, String toId){
        Member fromMember = memberRepository.findMemberById(fromId);
        Member toMember = memberRepository.findMemberById(toId);

        boolean result = followRepository.existsByFromMemberAndToMember(fromMember, toMember);
        return result;
    }

}
