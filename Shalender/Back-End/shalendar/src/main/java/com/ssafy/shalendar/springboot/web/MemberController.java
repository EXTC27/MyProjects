package com.ssafy.shalendar.springboot.web;

import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.domain.member.Role;
import com.ssafy.shalendar.springboot.help.BoolResult;
import com.ssafy.shalendar.springboot.help.StringResult;

import com.ssafy.shalendar.springboot.service.ChannelService;
import com.ssafy.shalendar.springboot.service.JwtService;
import com.ssafy.shalendar.springboot.service.MemberService;
import com.ssafy.shalendar.springboot.web.dto.channel.ChannelResponseDto;

import com.ssafy.shalendar.springboot.web.dto.concern.ConcernDeleteRequestDto;
import com.ssafy.shalendar.springboot.web.dto.concern.ConcernSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberLoginRequestDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberResponseDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberUpdateRequestDto;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/member")
@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin({"*"})
public class MemberController {
    private final MemberService mService;
    private final ChannelService cService;
    private final JwtService jwtService;
    @Value("${custom.path.upload-images}")
    private String uploadPath;


//    @GetMapping(value = "/getImage/{email}", produces = MediaType.IMAGE_JPEG_VALUE)
//    public ResponseEntity<Object> getImage(@PathVariable String email){
//        try {
//            InputStream in = getClass().getResourceAsStream("C:\\Users\\multicampus\\Desktop\\image\\member\\"+email+".jpg"); // 이거슨 윈도우용
////            InputStream in = getClass().getResourceAsStream("/home/ubuntu/images/member/"+email+".jpg"); // 이거슨 aws용
//            if (in != null) {
//                return new ResponseEntity<Object>(IOUtils.toByteArray(in), HttpStatus.OK);
//            } else {
//                return new ResponseEntity<Object>("No Image", HttpStatus.OK);
//            }
//        } catch (IOException e){
//            log.error("getImage", e);
//            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/uploadImage/{email}")
    public ResponseEntity<Object> uploadImage(@RequestBody MultipartFile file, @PathVariable String email) {
        log.trace("uploadImage: {}", email);
        StringResult nr = null;
        if (file != null) {
            List<HashMap> fileArrayList = new ArrayList<HashMap>();
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
//                System.out.println("multimedia/profile/"+fileName);
                mService.updatePath(email, "multimedia/profile/"+fileName);
            } catch (Exception e) {
                System.out.println("postTempFile_ERROR======>" + fileFullPath);
                e.printStackTrace();
            }
            nr = new StringResult("uploadImage", originalFilename, "SUCCESS");
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
            boolean result = mService.updatePath(email, null);
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
            boolean result = mService.isExist(id);

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
            boolean result = mService.isExistingNickName(nickname);
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
    public ResponseEntity<Object> confirmPassword(@RequestBody MemberLoginRequestDto requestDto){
        log.trace("confirmPassword: {}", requestDto);
        BoolResult br = null;
        try {
            boolean result = mService.confirmPassword(requestDto);
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
    public ResponseEntity<Object> signup(@RequestBody MemberSaveRequestDto member) {
        log.trace("sign up: {}", member);
        try {
            MemberResponseDto searchedMember = mService.searchMember(member.getId());
            ChannelResponseDto searchedChannel = cService.searchChannel(member.getId());
            StringResult nr = null;
            if (searchedMember == null && searchedChannel == null) {
                member.setRole(Role.USER);
                boolean result = mService.signup(member);
                if (result) {
                    nr = new StringResult("addMember", member.getId(), "SUCCESS");
                } else {
                    nr = new StringResult("addMember", "-1", "FAIL");
                }
            } else {
                nr = new StringResult("addMember", "Duplicate ID", "FAIL");
            }
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("addMember", e);
            throw e;
        }
    }

    @PostMapping("/jwt_auth/findMemberById/{id}")
    public ResponseEntity<Map<String, Object>> findMemberById(@PathVariable String id) {
        log.trace("findMemberById: {}", id);
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            MemberResponseDto member = mService.searchMember(id);

            resultMap.put("status", true);
            resultMap.put("info", member);
            status = HttpStatus.ACCEPTED;
        } catch (RuntimeException e) {
            log.error("멤버정보 조회 실패", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

//    @GetMapping("/findAllMembers")
//    public ResponseEntity<Object> findAllMembers() {
//        log.trace("findAllMembers");
//        try {
//
//            List<MemberResponseDto> members = mService.searchAll();
//            return new ResponseEntity<Object>(members, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            log.error("findAllMembers", e);
//            throw e;    // spring, tomcat이 받음
//        }
//    }

    @PutMapping("/updateMember")
    @ApiOperation(value = "회원정보를 수정한다.", response = BoolResult.class)
    public ResponseEntity<Object> updateMember(@RequestBody MemberUpdateRequestDto member) {
        log.trace("updateMember: {}", member);
        try {
            boolean result = mService.update(member.getId(), member);
            BoolResult br = null;
            if (result) {
                br = new BoolResult(true, "updateMember", "SUCCESS");
            } else {
                br = new BoolResult(false, "updateMember", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("updateMember", e);
            throw e;
        }
    }

    @DeleteMapping("/deleteMember/{id}")
    @ApiOperation(value = "회원정보를 삭제한다.", response = BoolResult.class)
    public ResponseEntity<Object> deleteMember(@PathVariable String id) {
        log.trace("deleteMember: {}", id);
        try {
            boolean result = mService.delete(id);
            BoolResult br = null;
            if (result) {
                br = new BoolResult(true, "deleteMember", "SUCCESS");
            } else {
                br = new BoolResult(false, "deleteMember", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("deleteMember", e);
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody MemberLoginRequestDto member) {    //HTTP요청의 내용을 객체에 매핑하기 위해 @RequestBody 를 설정.
        log.trace("login: {}", member);
        Map<String, Object> resultmap = new HashMap<>();
        HttpStatus status = null;
        try {
            MemberResponseDto mrd = mService.login(member.getId(), member.getPw());
            ChannelResponseDto crd = cService.login(member.getId(), member.getPw());
            if (mrd == null && crd == null) {
                resultmap.put("jwt", null);
                resultmap.put("status", false);
                resultmap.put("data", "-1");
            } else if (mrd != null){
                Member loginMember = mrd.toEntity();
                String token = jwtService.create(loginMember.getMem_no()+loginMember.getId());
                // 토큰 정보는 request의 헤더로 보내고 나머지는 Map에 담아주자.
//                res.setHeader("jwt", token);
                resultmap.put("jwt", token);
                resultmap.put("status", true);
                resultmap.put("data", loginMember);
            } else if (crd != null){
                Channel loginChannel = crd.toEntity();
                String token = jwtService.create(loginChannel.getCh_no()+loginChannel.getId());
                // 토큰 정보는 request의 헤더로 보내고 나머지는 Map에 담아주자.
//                res.setHeader("jwt-auth-token", token);
                resultmap.put("jwt", token);
                resultmap.put("status", true);
                resultmap.put("data", loginChannel);
            }
            status = HttpStatus.ACCEPTED;
        } catch (RuntimeException e) {
            log.error("로그인 실패");
            e.printStackTrace();
            resultmap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultmap, status);
    }

    @GetMapping("/getMyInterests/{id}")
    public ResponseEntity<Object> getMyInterest(@PathVariable String id) {
        log.trace("getMyInterests: {}", id);
        try {
            List<String> concerns = mService.getInterests(id);
            return new ResponseEntity<>(concerns, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("getMyConcerns", e);
            throw e;
        }
    }

    @PostMapping("/saveInterest")
    public ResponseEntity<Object> saveInterest(@RequestBody ConcernSaveRequestDto requestDto) {
        log.trace("saveInterests: {}", requestDto);
        try {
            BoolResult br = null;
            boolean result = mService.saveInterest(requestDto);
            if (result) {
                br = new BoolResult(true, "saveInterest", "SUCCESS");
            } else {
                br = new BoolResult(false, "saveInterest", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("saveInterest", e);
            throw e;
        }
    }

    @DeleteMapping("/deleteInterest/")
    public ResponseEntity<Object> deleteInterest(@RequestBody ConcernDeleteRequestDto requestDto) {
        log.trace("deleteInterests: {}", requestDto);
        try {
            BoolResult br = null;
            boolean result = mService.deleteInterest(requestDto);
            if (result) {
                br = new BoolResult(true, "deleteInterest", "SUCCESS");
            } else {
                br = new BoolResult(false, "saveInterest", "FAIL");
            }
            return new ResponseEntity<>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("deleteInterest", e);
            throw e;
        }
    }
}
