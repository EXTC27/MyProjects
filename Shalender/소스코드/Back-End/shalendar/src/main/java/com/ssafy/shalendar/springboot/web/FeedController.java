package com.ssafy.shalendar.springboot.web;

import com.ssafy.shalendar.springboot.help.BoolResult;
import com.ssafy.shalendar.springboot.help.LongResult;
import com.ssafy.shalendar.springboot.help.StringResult;
import com.ssafy.shalendar.springboot.service.FeedService;
import com.ssafy.shalendar.springboot.service.SubscribeService;
import com.ssafy.shalendar.springboot.web.dto.feed.*;
import com.ssafy.shalendar.springboot.web.dto.subscribe.SubscribeListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@RequestMapping("/feed")
@RequiredArgsConstructor
@RestController
@Slf4j
@CrossOrigin({"*"})
public class FeedController {
    private final FeedService feedService;
    private final SubscribeService subscribeService;
    @Value("${custom.path.upload-images}")
    private String uploadPath;

    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody FeedSaveRequestDto requestDto) {
        log.trace("saveFeed: {}", requestDto);
        LongResult lr = null;
        try {
            boolean result = feedService.save(requestDto);
            if (result) {
                Long feedNo = feedService.findRecentFeedNo(requestDto.getSchNo());
                lr = new LongResult("saveFeed", feedNo, "SUCCESS");
            } else {
                lr = new LongResult("saveFeed", -1, "FAIL");
            }
        } catch (RuntimeException e) {
            log.error("saveFeed", e);
            throw e;
        }
        return new ResponseEntity<Object>(lr, HttpStatus.OK);
    }

    @GetMapping("/feedList")
    public ResponseEntity<Object> feedList() {
        log.trace("feedList");
        try {
            List<FeedListResponseDto> feedList = feedService.findAllDesc();
            if (feedList == null) {
                return new ResponseEntity<Object>("There isn't any Feed you provide", HttpStatus.OK);
            }
            return new ResponseEntity<Object>(feedList, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("feedList", e);
            throw e;    // spring, tomcat이 받음
        }
    }

    @PostMapping("/moreFeedInfo")
    public ResponseEntity<Object> moreFeedInfo(@RequestBody FeedSubscribeRequestDto fsrDto) {
        log.trace("moreFeedInfo: {}", fsrDto);
        try {
            List<SubscribeListResponseDto> subscribeList = subscribeService.findAllSubscribe(fsrDto.getId());
            List<FeedSubscribeListDto> result =new LinkedList<>();
            if(subscribeList==null)
                result=feedService.feedInfoForChannel(fsrDto);
            else
                result=feedService.feedInfoForMember(subscribeList, fsrDto);
            if (result == null) {
                return new ResponseEntity<Object>("There isn't any Feed you provide", HttpStatus.OK);
            }
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("moreFeedInfo", e);
            throw e;    // spring, tomcat이 받음
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteFeed(@PathVariable Long id) {
        log.trace("deleteFeed: {}", id);
        BoolResult br = null;
        try {
            boolean result = feedService.delete(id);
            if (result) {
                br = new BoolResult(true, "deleteFeed", "SUCCESS");
            } else {
                br = new BoolResult(false, "deleteFeed", "FAIL");
            }
        } catch (RuntimeException e) {
            log.error("deleteFeed", e);
            throw e;
        }
        return new ResponseEntity<Object>(br, HttpStatus.OK);
    }

    @PostMapping("/uploadImage/{feedNo}")
    public ResponseEntity<Object> uploadImage(@RequestBody MultipartFile file, @PathVariable Long feedNo){
        log.trace("uploadFeedImage: {}", feedNo);
        StringResult nr = null;
        if (file != null) {
            List<HashMap> fileArrayList = new ArrayList<HashMap>();
            HashMap fileHashMap;

            String filePath = uploadPath + "feed";

            File dir = new File(filePath); //파일 저장 경로 확인, 없으면 만든다.
            if (!dir.exists()) {
                dir.mkdirs();
            }

            fileHashMap = new HashMap();
            String originalFilename = file.getOriginalFilename(); //파일명
            String fileName = feedNo + ".jpg";
            String fileFullPath = filePath + "/" + fileName; //파일 전체 경로
            try {
                //파일 저장
                file.transferTo(new File(fileFullPath)); //파일저장

                fileHashMap.put("fileName", fileName);
                fileHashMap.put("fileFullPath", fileFullPath);

                fileArrayList.add(fileHashMap);
                feedService.updatePath(feedNo, "multimedia/feed/"+feedNo+".jpg", 1);
            } catch (Exception e) {
                System.out.println("postTempFile_ERROR======>" + fileFullPath);
                e.printStackTrace();
            }
            nr = new StringResult("uploadImage", originalFilename, "SUCCESS");
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        } else {
            nr = new StringResult("uploadImage", "no image", "FAIL");
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        }
    }

    @PostMapping("/uploadVideo/{feedNo}")
    public ResponseEntity<Object> uploadVideo(@RequestBody MultipartFile file, @PathVariable Long feedNo){
        log.trace("uploadFeedVideo: {}", feedNo);
        StringResult nr = null;
        if (file != null) {
            List<HashMap> fileArrayList = new ArrayList<HashMap>();
            HashMap fileHashMap;

            String filePath = uploadPath + "feed";
            File dir = new File(filePath); //파일 저장 경로 확인, 없으면 만든다.
            if (!dir.exists()) {
                dir.mkdirs();
            }

            fileHashMap = new HashMap();
            String originalFilename = file.getOriginalFilename(); //파일명
            String fileName = feedNo + ".mp4";
            String fileFullPath = filePath + "/" + fileName; //파일 전체 경로
            try {
                //파일 저장
                file.transferTo(new File(fileFullPath)); //파일저장

                fileHashMap.put("fileName", fileName);
                fileHashMap.put("fileFullPath", fileFullPath);

                fileArrayList.add(fileHashMap);
                feedService.updatePath(feedNo, "multimedia/feed/"+feedNo+".mp4", 2);
            } catch (Exception e) {
                System.out.println("postTempFile_ERROR======>" + fileFullPath);
                e.printStackTrace();
            }
            nr = new StringResult("uploadVideo", originalFilename, "SUCCESS");
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        } else {
            nr = new StringResult("uploadVideo", "no video", "FAIL");
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        }
    }

    @DeleteMapping("/deleteImage/{feedNo}")
    public ResponseEntity<Object> deleteImage(@PathVariable Long feedNo){
        log.trace("deleteImage: {}", feedNo);
        BoolResult br = null;
        try {
            boolean result = feedService.updatePath(feedNo, null, 3);
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

    @DeleteMapping("/deleteVideo/{feedNo}")
    public ResponseEntity<Object> deleteVideo(@PathVariable Long feedNo){
        log.trace("deleteVideo: {}", feedNo);
        BoolResult br = null;
        try {
            boolean result = feedService.updatePath(feedNo, null, 4);
            if (result) {
                br = new BoolResult(true, "deleteVideo", "SUCCESS");
            } else {
                br = new BoolResult(false, "deleteVideo", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("deleteVideo", e);
            throw e;
        }
    }
}
