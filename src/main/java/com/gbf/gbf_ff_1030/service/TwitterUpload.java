package com.gbf.gbf_ff_1030.service;

import com.gbf.gbf_ff_1030.dto.PlayerDto;

public interface TwitterUpload {

    public void tweetTokenTest();
    public void tweetGetAccessTokenTest(String pin);
    public void sendTweetTest();
    public void sendPlayerTweet(PlayerDto playerDto, String message);

}
