package com.goormy.hackathon.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
public class FollowService {
    private static final Logger logger = LoggerFactory.getLogger(FollowService.class);


    @Autowired
    private SqsClient sqsClient;

    @Value("${spring.cloud.aws.sqs.queue-url}")
    private String queueUrl;

    public void sendFollowRequest(String userId, String hashtagId) {
        String messageBody = String.format("{\"userId\": \"%s\", \"hashtagId\": \"%s\"}", userId, hashtagId);
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();
        logger.info("Received follow request - userId: {}, hashtagId: {}", userId, hashtagId);
        try {
            SendMessageResponse sendMsgResponse = sqsClient.sendMessage(sendMsgRequest);
            logger.info("Message sent to SQS: {}, Message ID: {}, HTTP Status: {}",
                    messageBody, sendMsgResponse.messageId(), sendMsgResponse.sdkHttpResponse().statusCode());
        } catch (Exception e) {
            logger.error("Failed to send message to SQS: {}", messageBody, e);
        }
    }
}
