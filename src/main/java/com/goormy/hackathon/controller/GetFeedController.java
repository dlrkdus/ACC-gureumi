package com.goormy.hackathon.controller;

import com.goormy.hackathon.common.util.LocalDateTimeConverter_DS;
import com.goormy.hackathon.common.util.LocalDateTimeConverter_DS;
import com.goormy.hackathon.dto.request.AddFeedUser;
import com.goormy.hackathon.dto.response.GetFeedResponseDto;
import com.goormy.hackathon.redis.entity.PostSimpleInfo_DS;
import com.goormy.hackathon.repository.FeedHashtagRedisRepository_DS;
import com.goormy.hackathon.repository.FeedUserRedisRepository_DS;
import com.goormy.hackathon.service.GetFeedService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class GetFeedController {

    private final GetFeedService getFeedService;
    private final LocalDateTimeConverter_DS localDateTimeConverter;

    private final FeedUserRedisRepository_DS feedUserRedisRepository;
    private final FeedHashtagRedisRepository_DS feedHashtagRedisRepository;

    /**
     * [GET] 사용자 맞춤형 피드 조회 기능
     *
     * @param userId : 사용자 Id
     * @param size   : Pagenation size
     * @return : size만큼의 게시글을 반환
     */
    @GetMapping("/feed")
    public ResponseEntity<List<GetFeedResponseDto>> getFeedList(
        @RequestHeader Long userId,
        @RequestParam int size
    ) {
        // 피드 리스트 반환
        return ResponseEntity.ok(getFeedService.getFeedList(userId, size));
    }

    @PostMapping("/hashfeed")
    public ResponseEntity<String> addFeedHashtagData(
        @RequestHeader Long userId,
        @RequestBody AddFeedUser requestDto
    ) {
        requestDto.getInfoList().forEach(
            info ->
                feedHashtagRedisRepository.add(userId,
                    PostSimpleInfo_DS.toEntity(info.getPostId(),
                        localDateTimeConverter.convertToString(info.getCreatedAt())))
        );

        // 피드 리스트 반환
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/userfeed")
    public ResponseEntity<String> addFeedUserData(
        @RequestHeader Long userId,
        @RequestBody AddFeedUser requestDto
    ) {
        requestDto.getInfoList().forEach(
            info ->
                feedUserRedisRepository.add(userId,
                    PostSimpleInfo_DS.toEntity(info.getPostId(),
                        localDateTimeConverter.convertToString(info.getCreatedAt())))
        );

        // 피드 리스트 반환
        return ResponseEntity.ok("ok");
    }

}

