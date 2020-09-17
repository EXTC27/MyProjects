package com.ssafy.shalendar.springboot.service;

import com.ssafy.shalendar.springboot.domain.concern.Concern;
import com.ssafy.shalendar.springboot.domain.concern.ConcernRepository;
import com.ssafy.shalendar.springboot.domain.interest.Interest;
import com.ssafy.shalendar.springboot.domain.interest.InterestRepository;
import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.domain.member.MemberRepository;

import com.ssafy.shalendar.springboot.web.dto.channel.ChannelLoginRequestDto;
import com.ssafy.shalendar.springboot.web.dto.concern.ConcernDeleteRequestDto;
import com.ssafy.shalendar.springboot.web.dto.concern.ConcernSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberLoginRequestDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberResponseDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

    private static Logger logger = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;
    private final InterestRepository interestRepository;
    private final ConcernRepository concernRepository;

    public Boolean isExist(String id){
        try {
            return memberRepository.existsById(id);
        } catch (RuntimeException e){
            logger.error("존재여부 파악 실패", e);
            throw e;
        }
    }

    public Boolean isExistingNickName(String nickname){
        try {
            return memberRepository.existsByNickname(nickname);
        } catch (RuntimeException e){
            logger.error("채널 닉네임 존재 여부 파악 실패", e);
            throw e;
        }
    }

    public Boolean confirmPassword(MemberLoginRequestDto requestDto){
        try {
            return memberRepository.existsByIdAndPw(requestDto.getId(), requestDto.getPw());
        } catch (RuntimeException e) {
            logger.error("비밀번호 확인 실패", e);
            throw e;
        }
    }

    @Transactional
    public Boolean signup(MemberSaveRequestDto requestDto) {
        try {
            memberRepository.save(requestDto.toEntity()).getMem_no();

            Member member = memberRepository.findMemberById(requestDto.getId());

            List<String> interestsName = requestDto.getInterests();

            for (String interestName : interestsName) {
                Interest interest = interestRepository.findByInterestName(interestName);
                Concern newConcern = Concern.builder().member(member).interest(interest).build();
                concernRepository.save(newConcern);
            }
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    public MemberResponseDto searchMember(String id) {
        Member result = null;
        try {
            result = memberRepository.findMemberById(id);
        } catch (RuntimeException e) {
            logger.error("멤버 검색 실패", e);
        }
        if (result == null) {
            return null;
        } else {
            return new MemberResponseDto(result);
        }
    }

    public List<MemberResponseDto> searchAll() {
        List<Member> members = memberRepository.findAll();
        List<MemberResponseDto> result = new ArrayList<>();
        for (Member member : members){
            result.add(new MemberResponseDto(member));
        }
        return result;
    }

    @Transactional
    public Boolean update(String id, MemberUpdateRequestDto requestDto) {
        MemberResponseDto member = null;
        try {
            member = new MemberResponseDto(memberRepository.findMemberById(id));
            Member memberEntity = memberRepository.findById(member.getMem_no()).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

            if (member == null) {
                throw new RuntimeException("해당 아이디가 없습니다.");
            }
            memberEntity.update(requestDto);

            // 모든 관심사 삭제
            List<Concern> concerns = concernRepository.findAllByMember_Id(id);
            for (Concern concern : concerns) {
                Interest interest = concern.getInterest();
                concernRepository.deleteByMemberAndInterest(memberEntity, interest);
            }

            // 들어온 관심사로 등록
            List<String> interestsName = requestDto.getInterests();
            for (String interestName : interestsName) {
                Interest interest = interestRepository.findByInterestName(interestName);
                Concern newConcern = Concern.builder().member(memberEntity).interest(interest).build();
                concernRepository.save(newConcern);
            }
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public Boolean delete(String id) {
        MemberResponseDto member = null;
        try {
            member = new MemberResponseDto(memberRepository.findMemberById(id));
            if (member == null) {
                throw new RuntimeException("해당 아이디가 없습니다.");
            }
            memberRepository.delete(member.toEntity());
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional(readOnly = true)
    public MemberResponseDto login(String id, String pw) {
        Member member = memberRepository.findMemberByIdAndPw(id, pw);
        if (member == null){
            return null;
        } else {
            return new MemberResponseDto(member);
        }
    }

    public List<String> getInterests(String id){
        List<Concern> concerns = concernRepository.findAllByMember_Id(id);
        List<String> result = new ArrayList<>();
        for (Concern concern : concerns){
            result.add(concern.getInterest().getInterestName());
        }
        return result;
    }

    @Transactional
    public boolean saveInterest(ConcernSaveRequestDto requestDto){
        try {
            Member member = memberRepository.findMemberById(requestDto.getId());
            Interest interest = interestRepository.findByInterestName(requestDto.getInterest());

            Concern searchedConcern = concernRepository.findByMemberAndInterest(member, interest);
            if (searchedConcern == null) {
                Concern newConcern = Concern.builder().member(member).interest(interest).build();
                concernRepository.save(newConcern);
                return true;
            } else {
                return false;
            }
        } catch (RuntimeException e){
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean deleteInterest(ConcernDeleteRequestDto requestDto){
        try {
            Member member = memberRepository.findMemberById(requestDto.getId());
            Interest interest = interestRepository.findByInterestName(requestDto.getInterest());

            int result = concernRepository.deleteByMemberAndInterest(member, interest);
            if (result != 0) {
                return true;
            } else {
                return false;
            }
        } catch (RuntimeException e){
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean updatePath(String email, String path) {
        try {
            Member member = memberRepository.findMemberById(email);
            member.update(new MemberUpdateRequestDto(member.getMem_no(), member.getId(), member.getPw(), member.getNickname(), member.getBirth(), member.getNickname(), path, member.getLink(), member.getMsg(), member.getRole()));
            return true;
        } catch (RuntimeException e) {
            logger.error("updatePath", e);
            return false;
        }
    }
}
