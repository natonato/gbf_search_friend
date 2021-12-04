package com.gbf.gbf_ff.service;

import com.gbf.gbf_ff.dto.PlayerDto;

public interface TwitterUpload {

    public void tweetTokenTest();
    public void tweetGetAccessTokenTest(String pin);
    public void sendTweetTest();
    public void sendPlayerTweet(String id);

}
