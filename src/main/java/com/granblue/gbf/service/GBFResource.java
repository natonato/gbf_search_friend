package com.granblue.gbf.service;

import com.granblue.gbf.dto.PlayerDto;

import java.awt.*;
import java.io.IOException;


public interface GBFResource {
    public void getImg(String[][] imgUrl, String id, Graphics2D graphics);
    public void makeProfileImg(PlayerDto playerDto, String message, int imgType) throws IOException;
}
