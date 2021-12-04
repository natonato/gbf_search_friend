package com.gbf.gbf_ff.service;


import com.gbf.gbf_ff.dto.PlayerDto;

import java.io.IOException;
import java.text.ParseException;

public interface PlayerInfo {
    public void twitterCookieTest() throws InterruptedException, IOException, ParseException;
    public void gbfCookieTest() throws InterruptedException, IOException, ParseException;
    public PlayerDto resourceTest(String profileId) throws  IOException;
    public void twitterTest() throws InterruptedException, IOException;
    public void gbfTest() throws InterruptedException;

}
