package com.gbf.gbf_ff.service;

import com.gbf.gbf_ff.dto.PlayerDto;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface GBFResource {
//    public void getImg(String[][] imgUrl, String id, Graphics2D graphics);
    public void makeProfileImg(PlayerDto playerDto, String message, int imgType) throws IOException;
}
