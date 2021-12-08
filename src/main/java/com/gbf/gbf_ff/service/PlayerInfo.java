package com.gbf.gbf_ff.service;


import com.gbf.gbf_ff.dto.PlayerDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;

@Service
public interface PlayerInfo {
    public void twitterCookieTest() throws InterruptedException, IOException, ParseException;
    public void gbfCookieTest() throws InterruptedException, IOException, ParseException;
    public PlayerDto resourceTest(String profileId) throws  Exception;
    public String[] getTwitterMessage(String code);
}
