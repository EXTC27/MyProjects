package com.ssafy.shalendar.springboot.web;

import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.channel.Role;
import com.ssafy.shalendar.springboot.help.BoolResult;
import com.ssafy.shalendar.springboot.help.StringResult;

import com.ssafy.shalendar.springboot.service.ChannelService;
import com.ssafy.shalendar.springboot.service.JwtService;
import com.ssafy.shalendar.springboot.service.MemberService;

import com.ssafy.shalendar.springboot.web.dto.channel.*;
import com.ssafy.shalendar.springboot.web.dto.concern.ConcernDeleteRequestDto;
import com.ssafy.shalendar.springboot.web.dto.concern.ConcernSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/channel")
@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin({"*"})
public class ChannelController {

    private final MemberService mService;
    private final ChannelService cService;
    private final JwtService jwtService;
    @Value("${custom.path.upload-images}")
    private String uploadPath;

    @PostMapping("/uploadImage/{email}")
    public ResponseEntity<Object> uploadImage(@RequestBody MultipartFile file, @PathVariable String email){
        log.trace("uploadImage: {}", email);
        StringResult nr = null;
        if (file != null) {
            List<HashMap> fileArrayList = new ArrayList<>();
            HashMap fileHashMap;

            String filePath = uploadPath + "profile";

            File dir = new File(filePath); //파일 저장 경로 확인, 없으면 만든다.
            if (!dir.exists()) {
                dir.mkdirs();
            }

            fileHashMap = new HashMap();
            String originalFilename = file.getOriginalFilename(); //파일명

            String fileName = email + ".jpg";
            String fileFullPath = filePath + "/" + fileName; //파일 전체 경로

            try {
                //파일 저장
                file.transferTo(new File(fileFullPath)); //파일저장

                fileHashMap.put("fileName", fileName);
                fileHashMap.put("fileFullPath", fileFullPath);

                fileArrayList.add(fileHashMap);
                nr = new StringResult("uploadImage", originalFilename, "SUCCESS");
                cService.updatePath(email, "multimedia/profile/"+fileName);
            } catch (Exception e) {
                System.out.println("postTempFile_ERROR======>" + fileFullPath);
                e.printStackTrace();
                nr = new StringResult("uploadImage", "-1", "FAIL");
            }
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        } else {
            nr = new StringResult("uploadImage", "no image", "SUCCESS");
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        }
    }

    @DeleteMapping("/deleteImage/{email}")
    public ResponseEntity<Object> deleteImage(@PathVariable String email) {
        log.trace("deleteImage: {}", email);
        BoolResult br = null;
        try {
            boolean result = cService.updatePath(email, null);
            if (result) {
                br = new BoolResult(true, "deleteImage", "SUCCESS");
            } else {
                br = new BoolResult(false, "deleteImage", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("deleteImage", e);
            throw e;
        }
    }

    @GetMapping("/isExist/{id}")
    public ResponseEntity<Object> isExist(@PathVariable String id) {
        log.trace("isExist: {}", id);
        BoolResult br = null;
        try {
            boolean result = cService.isExist(id);
            if (!result) {
                br = new BoolResult(true, "isExist", "SUCCESS");
            } else {
                br = new BoolResult(false, "isExist", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("isExist", e);
            throw e;
        }
    }

    @GetMapping("/isExistingNickName/{nickname}")
    public ResponseEntity<Object> isExistingNickName(@PathVariable String nickname) {
        log.trace("isExistingNickname: {}", nickname);
        BoolResult br = null;
        try {
            boolean result = cService.isExistingNickName(nickname);
            if (!result) {
                br = new BoolResult(true, "isExistingNickname", "SUCCESS");
            } else {
                br = new BoolResult(false, "isExistingNickname", "FAIL" );
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("isExistingNickname", e);
            throw e;
        }
    }

    @PostMapping("/confirmPassword")
    public ResponseEntity<Object> confirmPassword(@RequestBody ChannelLoginRequestDto requestDto){
        log.trace("confirmPassword: {}", requestDto);
        BoolResult br = null;
        try {
            boolean result = cService.confirmPassword(requestDto);
            if (result) {
                br = new BoolResult(true, "confirmPassword", "SUCCESS");
            } else {
                br = new BoolResult(false, "confirmPassword", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("confirmPassword", e);
            throw e;
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody ChannelSaveRequestDto channel) {
        log.trace("sign up: {}", channel);
        try {
            ChannelResponseDto searchedChannel = cService.searchChannel(channel.getId());
            MemberResponseDto searchedMember = mService.searchMember(channel.getId());
            StringResult nr = null;
            if (searchedChannel == null && searchedMember == null) {
                channel.setRole(Role.USER);

                boolean result = cService.signup(channel);
                if (result) {
                    nr = new StringResult("addChannel", channel.getId(), "SUCCESS");
                } else {
                    nr = new StringResult("addChannel", "-1", "FAIL");
                }
            } else {
                nr = new StringResult("addChannel", "Duplicate ID", "FAIL");
            }
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        } catch(RuntimeException e) {
            log.error("addChannel", e);
            throw e;
        }
    }

    @PostMapping("/jwt_auth/findChannelById/{id}")
    public ResponseEntity<Map<String, Object>> findChannelById(@PathVariable String id) {
        log.trace("findChannelById: {}", id);
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            ChannelResponseDto channel = cService.searchChannel(id);

            resultMap.put("status", true);
            resultMap.put("info", channel);
            status = HttpStatus.ACCEPTED;
        } catch (RuntimeException e) {
            log.error("채널정보 조회 실패", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

//    @GetMapping("/findAllChannels")
//    public ResponseEntity<Object> findAllChannels() {
//        log.trace("findAllChannels");
//        try {
//            List<Channel> channels = cService.searchAll();
//            for (int i = 0; i < channels.size(); i++){
//                System.out.println(channels.get(i).getId() + " " + channels.get(i).getNickname());
//            }
//            return new ResponseEntity<Object>(channels, HttpStatus.OK);
//        } catch(RuntimeException e) {
//            log.error("findAllChannels", e);
//            throw e;	// spring, tomcat이 받음
//        }
//    }

    @PutMapping("/updateChannel")
    @ApiOperation(value="채널정보를 수정한다.", response= BoolResult.class)
    public ResponseEntity<Object> updateChannel(@RequestBody ChannelUpdateRequestDto channel){
        log.trace("updateChannel: {}", channel);
        try {
            boolean result = cService.update(channel.getId(), channel);
            BoolResult br = null;
            if (result) {
                br = new BoolResult(true, "updateChannel", "SUCCESS");
            } else {
                br = new BoolResult(false, "updateChannel", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch(RuntimeException e) {
            log.error("updateChannel", e);
            throw e;
        }
    }

    @DeleteMapping("/deleteChannel/{id}")
    @ApiOperation(value="채널정보를 삭제한다.", response=BoolResult.class)
    public ResponseEntity<Object> deleteChannel(@PathVariable String id){
        log.trace("deleteChannel: {}", id);
        try {
            boolean result = cService.delete(id);
            BoolResult br = null;
            if (result) {
                br = new BoolResult(true, "deleteChannel", "SUCCESS");
            } else {
                br = new BoolResult(false, "deleteChannel", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch(RuntimeException e) {
            log.error("deleteChannel", e);
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody ChannelLoginRequestDto channel) {    //HTTP요청의 내용을 객체에 매핑하기 위해 @RequestBody 를 설정.
        log.trace("login: {}", channel);
        Map<String, Object> resultmap = new HashMap<>();
        HttpStatus status = null;
        try {
            ChannelResponseDto crd = cService.login(channel.getId(), channel.getPw());
            if (crd == null) {
                resultmap.put("jwt", null);
                resultmap.put("status", false);
                resultmap.put("data", "-1");
            } else {
                Channel loginChannel = crd.toEntity();
                String token = jwtService.create(loginChannel.getId()+loginChannel.getCh_no());
                // 토큰 정보는 request의 헤더로 보내고 나머지는 Map에 담아주자.
//                res.setHeader("jwt", token);
                resultmap.put("jwt", token);
                resultmap.put("status", true);
                resultmap.put("data", loginChannel);
            }
            status = HttpStatus.ACCEPTED;
        } catch (RuntimeException e){
            log.error("로그인 실패");
            resultmap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultmap, status);
    }

    @GetMapping("/getMyInterests/{id}")
    public ResponseEntity<Object> getMyInterest(@PathVariable String id){
        log.trace("getMyInterests: {}", id);
        try {
            List<String> concerns = cService.getInterests(id);
            return new ResponseEntity<>(concerns, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("getMyConcerns", e);
            throw e;
        }
    }

    @PostMapping("/saveInterest")
    public ResponseEntity<Object> saveInterest(@RequestBody ConcernSaveRequestDto requestDto){
        log.trace("saveInterest: {}", requestDto);
        try {
            BoolResult br = null;
            boolean result = cService.saveInterest(requestDto);
            if (result){
                br = new BoolResult(true, "saveInterest", "SUCCESS");
            } else {
                br = new BoolResult(false, "saveInterest", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("saveInterest", e);
            throw e;
        }
    }

    @DeleteMapping("/deleteInterest/")
    public ResponseEntity<Object> deleteInterest(@RequestBody ConcernDeleteRequestDto requestDto){
        log.trace("deleteInterest: {}", requestDto);
        try {
            BoolResult br = null;
            boolean result = cService.deleteInterest(requestDto);
            if (result){
                br = new BoolResult(true, "deleteInterest", "SUCCESS");
            } else {
                br = new BoolResult(false, "saveInterest", "FAIL");
            }
            return new ResponseEntity<>(br, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("deleteInterest", e);
            throw e;
        }
    }

    @GetMapping("/getPopularChannelsWithId/{memberId}")
    public ResponseEntity<Object> getPopularChannelsWithId(@PathVariable String memberId){
        log.trace("getPopularChannelsWithId: {}", memberId);
        try {
            List<ChannelRecommendedResponseDto> popularChannels = cService.getPopularChannels(memberId);
            return new ResponseEntity<Object>(popularChannels, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("getPopularChannelsWithId", e);
            throw e;
        }
    }

    @GetMapping("/getPopularChannels")
    public ResponseEntity<Object> getPopularChannels(){
        log.trace("getPopularChannels");
        try {
            List<ChannelRecommendedResponseDto> popularChannels = cService.getPopularChannels(null);
            return new ResponseEntity<Object>(popularChannels, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("getPopularChannels", e);
            throw e;
        }
    }

    @GetMapping("/getRecommendedChannelsWithId/{memberId}")
    public ResponseEntity<Object> getRecommendedChannelsWithId(@PathVariable String memberId) {
        log.trace("getRecommendedChannelsWithId: {}", memberId);
        try {
            List<ChannelRecommendedResponseDto> result = cService.getRecommendedChannelsWithId(memberId);
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("getRecommendedChannelsWithId", e);
            throw e;
        }
    }

    @GetMapping("/getRecommendedChannels")
    public ResponseEntity<Object> getRecommendedChannels() {
        log.trace("getRecommendedChannels");
        try {
            List<ChannelRecommendedResponseDto> result = cService.getRecommendedChannels();
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("getRecommendedChannels", e);
            throw e;
        }
    }

    @GetMapping("/searchChannelByNickname/{nickname}")
    public ResponseEntity<Object> searchChannelByNickname(@PathVariable String nickname) {
        log.trace("searchChannelByNickname: {}", nickname);
        try {
            List<ChannelSearchedResponseDto> searchedChannelsByNickname = cService.searchAllByNickname(nickname);
            return new ResponseEntity<Object>(searchedChannelsByNickname, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("searchChannelByNickname", e);
            throw e;
        }
    }

    @PutMapping("/updateSearchFrequency/{channelId}")
    public ResponseEntity<Object> updateSearchFrequency(@PathVariable String channelId) {
        log.trace("updateSearchFrequency: {}", channelId);
        BoolResult br = null;
        try {
            boolean result = cService.updateSearchFrequency(channelId);
            if (result) {
                br = new BoolResult(true, "updateSearchFrequency", "SUCCESS");
            } else {
                br = new BoolResult(false, "updateSearchFrequency", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("updateSearchFrequency", e);
            throw e;
        }
    }
}
