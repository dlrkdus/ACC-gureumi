package com.goormy.hackathon.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PhotoService {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.cloudfront.distribution-domain}")
    private String domain;

    // PreSigne_ure 생성함수
    public String getPreSignedUrl(Long prefix, String imageName, HttpMethod httpMethod) {
        imageName = createPath(prefix.toString(), imageName);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(bucket,
                imageName, httpMethod);
        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String bucket, String fileName,
                                                                       HttpMethod httpMethod) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(httpMethod)
                        .withExpiration(getPresignedUrlExpiration());
        return generatePresignedUrlRequest;
    }

    private String createPath(String prefix, String fileName) {
        return String.format("%s/%s", prefix, fileName);
    }

    // CDN Url 생성함수
    public String getCDNUrl(Long postId, String imageUrl) {
        return domain + "/" + postId.toString() + "/" + imageUrl;
    }

    private Date getPresignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
