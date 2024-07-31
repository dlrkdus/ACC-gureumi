package com.goormy.hackathon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goormy.hackathon.lambda.LikeFunction;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
public class LikeService {

    private static final Logger logger = LoggerFactory.getLogger(LikeFunction.class);

    @Autowired
    private SqsClient sqsClient;

    @Value("${spring.cloud.aws.sqs.queue-url}")
    private String queueUrl;

    public void sendLikeRequest(Long userId, Long postId) {
        sendRequest(userId, postId, "like");
    }

    public void sendCancelLikeRequest(Long userId, Long postId) {
        sendRequest(userId, postId, "unlike");
    }

    public void sendRequest(Long userId, Long postId, String action) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String messageBody = objectMapper.writeValueAsString(Map.of(
                "userId", userId,
                "postId", postId,
                "action", action
            ));
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();

            logger.info("메시지 송신 - action: {}, user Id: {}, postId: {}", action, userId, postId);
            SendMessageResponse sendMsgResponse = sqsClient.sendMessage(sendMsgRequest);
            logger.info("메시지가 전달되었습니다: {}, Message ID: {}, HTTP Status: {}",
                messageBody, sendMsgResponse.messageId(), sendMsgResponse.sdkHttpResponse().statusCode());
        } catch (Exception e) {
            logger.error("메시지 전송 실패", e);
        }
    }
}
