package com.goormy.hackathon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.Map;

@Service
public class FollowService {
    private static final Logger logger = LoggerFactory.getLogger(FollowService.class);


    @Autowired
    private SqsClient sqsClient;

    @Value("${spring.cloud.aws.sqs.queue-url}")
    private String queueUrl;

    public void sendFollowRequest(long userId, long hashtagId) {
        sendRequest(userId, hashtagId, "follow");
    }

    public void sendUnfollowRequest(long userId, long hashtagId) {
        sendRequest(userId, hashtagId, "unfollow");
    }

    public void sendRequest(long userId, long hashtagId, String action) {
        try{
        ObjectMapper objectMapper = new ObjectMapper();
        String messageBody = objectMapper.writeValueAsString(Map.of(
                "userId", userId,
                "hashtagId", hashtagId,
                "action",action
        ));
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();
        logger.info("메시지 송신 - action: {}, userId: {}, hashtagId: {}", action, userId, hashtagId);
        SendMessageResponse sendMsgResponse = sqsClient.sendMessage(sendMsgRequest);
        logger.info("메시지가 전달되었습니다: {}, Message ID: {}, HTTP Status: {}",
                messageBody, sendMsgResponse.messageId(), sendMsgResponse.sdkHttpResponse().statusCode());
        } catch (Exception e) {
            logger.error("메시지 전송 실패", e);
        }
    }
}
