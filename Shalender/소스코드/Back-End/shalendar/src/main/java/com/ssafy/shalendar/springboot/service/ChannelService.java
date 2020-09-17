package com.ssafy.shalendar.springboot.service;

import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.channel.ChannelRepository;

import com.ssafy.shalendar.springboot.domain.concern.Concern;
import com.ssafy.shalendar.springboot.domain.concern.ConcernRepository;
import com.ssafy.shalendar.springboot.domain.concerned.Concerned;
import com.ssafy.shalendar.springboot.domain.concerned.ConcernedRepository;
import com.ssafy.shalendar.springboot.domain.interest.Interest;
import com.ssafy.shalendar.springboot.domain.interest.InterestRepository;

import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.domain.member.MemberRepository;
import com.ssafy.shalendar.springboot.domain.subscribe.SubscribeRepository;
import com.ssafy.shalendar.springboot.help.ChannelToSubscriber;
import com.ssafy.shalendar.springboot.web.dto.channel.*;
import com.ssafy.shalendar.springboot.web.dto.concern.ConcernDeleteRequestDto;
import com.ssafy.shalendar.springboot.web.dto.concern.ConcernSaveRequestDto;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ChannelService {

    private static Logger logger = LoggerFactory.getLogger(ChannelService.class);

    private final ChannelRepository channelRepository;
    private final InterestRepository interestRepository;
    private final ConcernRepository concernRepository;
    private final ConcernedRepository concernedRepository;
    private final SubscribeRepository subscribeRepository;
    private final MemberRepository memberRepository;

    public Boolean isExist(String id){
        try {
            return channelRepository.existsById(id);
        } catch (RuntimeException e){
            logger.error("존재여부 파악 실패", e);
            throw e;
        }
    }

    public Boolean isExistingNickName(String nickname){
        try {
            return channelRepository.existsByNickname(nickname);
        } catch (RuntimeException e){
            logger.error("채널 닉네임 존재 여부 파악 실패", e);
            throw e;
        }
    }

    public Boolean confirmPassword(ChannelLoginRequestDto requestDto){
        try {
            return channelRepository.existsByIdAndPw(requestDto.getId(), requestDto.getPw());
        } catch (RuntimeException e) {
            logger.error("비밀번호 확인 실패", e);
            throw e;
        }
    }

    @Transactional
    public Boolean signup(ChannelSaveRequestDto requestDto) {
        try {
            requestDto.setSearchFrequency(0l);
            channelRepository.save(requestDto.toEntity()).getCh_no();
            Channel channel = channelRepository.findChannelById(requestDto.getId());
            List<String> interestsName = requestDto.getInterests();

            for (String interestName : interestsName) {
                Interest interest = interestRepository.findByInterestName(interestName);
                Concerned newConcerned = Concerned.builder().channel(channel).interest(interest).build();
                concernedRepository.save(newConcerned);
            }
            return true;
        } catch (RuntimeException e){
            logger.error("추가 실패");
            e.printStackTrace();
            return false;
        }
    }

    public ChannelResponseDto searchChannel(String id){
        Channel result = null;
        try {
            result = channelRepository.findChannelById(id);
        } catch (RuntimeException e){
            logger.error("검색 실패", e);
        }
        if (result == null){
            return null;
        } else {
            return new ChannelResponseDto(result);
        }
    }

    public List<Channel> searchAll() {
        List<Channel> result = channelRepository.findAll();
        return result;
    }

    @Transactional
    public Boolean update(String id, ChannelUpdateRequestDto requestDto) {
        ChannelResponseDto channel = null;
        try {
            channel = new ChannelResponseDto(channelRepository.findChannelById(id));
            Channel channelEntity = channelRepository.findById(channel.getCh_no()).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));;
            if (channel == null){
                throw new RuntimeException("해당 아이디가 없습니다.");
            }
            channelEntity.update(requestDto);

            // 모든 관심사 삭제
            List<Concerned> concerneds = concernedRepository.findAllByChannelId(id);
            for (Concerned concerned : concerneds) {
                Interest interest = concerned.getInterest();
                concernedRepository.deleteByChannelAndInterest(channelEntity, interest);
            }

            // 들어온 관심사로 등록
            List<String> interestsName = requestDto.getInterests();
            for (String interestName : interestsName) {
                Interest interest = interestRepository.findByInterestName(interestName);
                Concerned newConcerned = Concerned.builder().channel(channelEntity).interest(interest).build();
                concernedRepository.save(newConcerned);
            }
            return true;
        } catch (RuntimeException e){
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public Boolean delete (String id) {
        ChannelResponseDto channel = null;
        try {
            channel = new ChannelResponseDto(channelRepository.findChannelById(id));
            if (channel == null){
                throw new RuntimeException("해당 아이디가 없습니다.");
            }
            channelRepository.delete(channel.toEntity());
            return true;
        } catch (RuntimeException e){
            e.printStackTrace();
            return false;
        }
    }

    @Transactional(readOnly = true)
    public ChannelResponseDto login(String id, String pw) {
        Channel channel = channelRepository.findChannelByIdAndPw(id, pw);
        if (channel == null){
            return null;
        } else {
            return new ChannelResponseDto(channel);
        }
    }

    public List<String> getInterests(String id){
        List<Concerned> concerneds = concernedRepository.findAllByChannelId(id);
        List<String> result = new ArrayList<>();
        for (Concerned concerned : concerneds){
            result.add(concerned.getInterest().getInterestName());
        }
        return result;
    }

    @Transactional
    public boolean saveInterest(ConcernSaveRequestDto requestDto){
        try {
            Channel channel = channelRepository.findChannelById(requestDto.getId());
            Interest interest = interestRepository.findByInterestName(requestDto.getInterest());

            Concerned searchedConcerned = concernedRepository.findByChannelAndAndInterest(channel, interest);
            if (searchedConcerned == null){
                Concerned newConcerned = Concerned.builder().channel(channel).interest(interest).build();
                concernedRepository.save(newConcerned);
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
            Channel channel = channelRepository.findChannelById(requestDto.getId());
            Interest interest = interestRepository.findByInterestName(requestDto.getInterest());

            int result = concernedRepository.deleteByChannelAndInterest(channel, interest);
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

    public List<ChannelRecommendedResponseDto> getPopularChannels(String id){
        // 채널을 모두 불러옴
        List<Channel> channelList = channelRepository.findAll();
        // 구독자 순으로 정렬할 리스트
        List<ChannelToSubscriber> sortedListByCountOfSubscriber = new ArrayList<>();
        for (Channel channel : channelList) {
            // 구독자 수 받아서
            int subscriberCount = subscribeRepository.countAllByToChannel(channel);
            // 채널 이름과 함께 리스트에 넣기
            sortedListByCountOfSubscriber.add(new ChannelToSubscriber(channel.getId(), subscriberCount));
        }
        // 구독자 순으로 내림차순 정렬
        Collections.sort(sortedListByCountOfSubscriber, new Comparator<ChannelToSubscriber>() {
            @Override
            public int compare(ChannelToSubscriber channelToSubscriber, ChannelToSubscriber t1) {
                return t1.getCountSubscriber() - channelToSubscriber.getCountSubscriber();
            }
        });
        // 인기 채널의 이름을 저장할 리스트
        List<String> popularList = new ArrayList<>();
        // 최대 20위까지의 인기 채널을 저장
        for (int i = 0; i < sortedListByCountOfSubscriber.size(); i++){
            ChannelToSubscriber cts = sortedListByCountOfSubscriber.get(i);
            if (i == 20 || cts.getCountSubscriber() == 0) break;
            popularList.add(cts.getChannelId());
        }

        // id가 넘어올 경우 member 객체를 가져옴
        Member member = null;
        if (id != null) {
            member = memberRepository.findMemberById(id);
        }
        // 인기 채널의 필요 정보를 담아 보낼 리스트
        List<ChannelRecommendedResponseDto> result = new ArrayList<>();
        // 인기채널의 각 채널이름을 이용해서
        for (String channelId : popularList){
            // 채널 정보를 가져옴
            Channel channel = channelRepository.findChannelById(channelId);
            // 채널의 카테고리들을 가져옴
            List<Concerned> concerneds = concernedRepository.findAllByChannelId(channelId);
            // 채널의 카테고리를 string 리스트로 관리
            List<String> strConcerneds = new ArrayList<>();

            for (Concerned concerned : concerneds) {
                strConcerneds.add(concerned.getInterest().getInterestName());
            }
            // id가 넘어왔을 경우 반환할 정보에 해당 id가 구독했는지 여부를 담아 보낸다.
            if (member != null) {
                Boolean isSubscribe = subscribeRepository.existsByFromMemberAndToChannel(member, channel);
                result.add(new ChannelRecommendedResponseDto(channel, strConcerneds, isSubscribe));
            } else {
                result.add(new ChannelRecommendedResponseDto(channel, strConcerneds, false));
            }
        }

        return result;
    }

    public List<ChannelRecommendedResponseDto> getRecommendedChannelsWithId(String memberId){
        Member searchedMember = memberRepository.findMemberById(memberId);
        Channel searchedChannel = channelRepository.findChannelById(memberId);
        List<Concern> mem_concerns = null;
        List<Concerned> ch_concerneds = null;
        List<String> strConcerns = new ArrayList<>();
        if (searchedMember != null) {
            mem_concerns = concernRepository.findAllByMember_Id(memberId); // 회원 관심사 가져옴
            for (Concern concern : mem_concerns) {
                strConcerns.add(concern.getInterest().getInterestName());
            }
        } else if (searchedChannel != null) {
            ch_concerneds = concernedRepository.findAllByChannelId(memberId);
            for (Concerned concerned : ch_concerneds) {
                strConcerns.add(concerned.getInterest().getInterestName());
            }
        }

        List<Channel> channels = channelRepository.findAll(); // 채널 다 가져옴
        List<ChannelRecommendedResponseDto> result = new ArrayList<>(); // 겹치면 여기다 박자!
next:   for (Channel channel : channels) {
            // 해당 채널을 이미 구독 중이면 SKIP
            boolean isSubscribing = subscribeRepository.existsByFromMemberAndToChannel(searchedMember, channel);
            if (isSubscribing) continue next;
            // 각 채널의 카테고리 리스트 가져옴
            List<Concerned> concerneds = concernedRepository.findAllByChannelId(channel.getId());
            // 각 채널의 카테고리 리스트를 String 화
            List<String> strConcerneds = new ArrayList<>();
            for (Concerned concerned : concerneds) {
                strConcerneds.add(concerned.getInterest().getInterestName());
            }
            for (String channelInterest : strConcerneds) {
                for (String memberInterest : strConcerns) {
                    if (channelInterest.equals(memberInterest)) {
                        result.add(new ChannelRecommendedResponseDto(channel, strConcerneds, false));
                        continue next;
                    }
                }
            }

            Collections.sort(result, new Comparator<ChannelRecommendedResponseDto>() {
                @Override
                public int compare(ChannelRecommendedResponseDto channelRecommendedResponseDto, ChannelRecommendedResponseDto t1) {
                    Channel channel1 = channelRepository.findChannelById(channelRecommendedResponseDto.getId());
                    Channel channel2 = channelRepository.findChannelById(t1.getId());
                    int count1 = subscribeRepository.countAllByToChannel(channel1);
                    int count2 = subscribeRepository.countAllByToChannel(channel2);
                    return count2 - count1;
                }
            });
        }
        return result;
    }

    public List<ChannelRecommendedResponseDto> getRecommendedChannels(){
        List<Channel> channels = channelRepository.findAll(); // 채널 다 가져옴
        List<ChannelRecommendedResponseDto> result = new ArrayList<>(); // 랜덤으로 뽑은 거 여기다 박자
        List<Integer> picks = new ArrayList<>();
        int countOfChannels = channels.size();

        while (true) {
            int number = (int)(Math.random()*countOfChannels)+1;
            if (!picks.contains(number-1)) {
                picks.add(number - 1);
            }
            if (picks.size() == channels.size() || picks.size() == 20){
                break;
            }
        }

        for (Integer pick : picks) {
            List<Concerned> concerneds = concernedRepository.findAllByChannelId(channels.get(pick).getId());
            List<String> strConcerneds = new ArrayList<>();
            for (Concerned concerned : concerneds) {
                strConcerneds.add(concerned.getInterest().getInterestName());
            }
            result.add(new ChannelRecommendedResponseDto(channels.get(pick), strConcerneds, false));
        }
        return result;
    }

    public List<ChannelSearchedResponseDto> searchAllByNickname(String nickname) {
        List<Channel> channels = channelRepository.findAllByNicknameLikeOrderBySearchFrequencyDesc(nickname);
        List<ChannelSearchedResponseDto> searchedChannelsByNickname = new ArrayList<>();
        for (Channel channel : channels) {
            List<Concerned> interests = concernedRepository.findAllByChannelId(channel.getId());
            List<String> strInterests = new ArrayList<>();
            for (Concerned concerned : interests) {
                strInterests.add(concerned.getInterest().getInterestName());
            }
            searchedChannelsByNickname.add(new ChannelSearchedResponseDto(channel, strInterests));
        }
        return searchedChannelsByNickname;
    }

    @Transactional
    public Boolean updateSearchFrequency(String channelId) {
        try {
            Channel channel = channelRepository.findChannelById(channelId);
            channel.update(new ChannelUpdateRequestDto(channel.getCh_no(), channel.getId(), channel.getPw(), channel.getName(), channel.getNickname(), channel.getImg(), channel.getLink(), channel.getMsg(), channel.getPoster(), channel.getRole(), channel.getSearchFrequency() + 1));
            return true;
        } catch (RuntimeException e) {
            logger.error("검색빈도수 업데이트 실패", e);
            return false;
        }
    }

    @Transactional
    public boolean updatePath(String email, String path) {
        try {
            Channel channel = channelRepository.findChannelById(email);
            channel.update(new ChannelUpdateRequestDto(channel.getCh_no(), channel.getId(), channel.getPw(), channel.getName(), channel.getNickname(), path, channel.getLink(), channel.getMsg(), channel.getPoster(), channel.getRole(), channel.getSearchFrequency()));
            return true;
        } catch (RuntimeException e) {
            logger.error("updatePath", e);
            return false;
        }
    }
}
