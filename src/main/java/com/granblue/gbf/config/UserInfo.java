package com.granblue.gbf.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserInfo {

    @Value("${twitterID}")
    private String twitterID;

    @Value("${twitterPW}")
    private String twitterPW;

}
