package com.gbf.gbf_ff_1030.service;

import com.gbf.gbf_ff_1030.dto.PlayerDto;

import java.awt.*;
import java.io.IOException;


public interface GBFResource {
//    public void getImg(String[][] imgUrl, String id, Graphics2D graphics);
    public void makeProfileImg(PlayerDto playerDto, String message, int imgType) throws IOException;
}
