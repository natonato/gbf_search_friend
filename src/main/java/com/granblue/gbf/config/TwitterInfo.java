package com.granblue.gbf.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Data
@Component
public class TwitterInfo {

    @Value("${APIKey}")
    private String APIKey;

    @Value("${APISecretKey}")
    private String APISecretKey;

    @Value("${BearerToken}")
    private String BearerToken;

    @Value("${accessToken}")
    private String accessToken;

    @Value("${accessSecret}")
    private String accessSecret;

}
