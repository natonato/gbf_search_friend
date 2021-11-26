package com.granblue.gbf.service;

import com.granblue.gbf.dto.PlayerDto;

import java.io.File;

public interface TwitterUpload {
    public void tweetTokenTest();
    public void tweetGetAccessTokenTest(String pin);
    public void sendTweetTest();
    public void sendPlayerTweet(PlayerDto playerDto, String message, File image);
}
