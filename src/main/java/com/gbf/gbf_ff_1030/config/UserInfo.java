package com.gbf.gbf_ff_1030.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserInfo {

    @Value("${twitterID}")
    private String twitterID;

    @Value("${twitterPW}")
    private String twitterPW;

}
