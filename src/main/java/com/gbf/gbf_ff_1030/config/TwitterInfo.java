package com.gbf.gbf_ff_1030.config;

import com.gbf.gbf_ff_1030.service.TwitterUpload;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Data
@Component
public class TwitterInfo {

//    private static TwitterInfo twitterInfo;
//    private TwitterInfo(){ }
//    public static TwitterInfo getInstance(){
//        if(twitterInfo==null)twitterInfo=new TwitterInfo();
//        return twitterInfo;
//    }

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
