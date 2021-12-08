package com.gbf.gbf_ff.service;


import org.springframework.stereotype.Service;

@Service
public interface TwitterUpload {

    public void sendPlayerTweet(String id, String msg)throws Exception;

}
