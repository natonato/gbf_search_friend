package com.gbf.gbf_ff.controller;

import com.gbf.gbf_ff.Exception.DuplicatedUserException;
import com.gbf.gbf_ff.dto.PlayerDto;
import com.gbf.gbf_ff.service.GBFResource;
import com.gbf.gbf_ff.service.PlayerInfo;
import com.gbf.gbf_ff.service.TwitterUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;

@Controller
public class MainController {

    private final PlayerInfo playerInfo;

    private final TwitterUpload twitterUpload;

//    private final GBFResource gbfResource;

    @Autowired
    MainController(PlayerInfo playerInfo, TwitterUpload twitterUpload
                   ){
        this.twitterUpload=twitterUpload;
//        this.gbfResource=gbfResource;
        this.playerInfo = playerInfo;
    }

    @GetMapping("/")
    public String getHome(){
        return "index";
    }


    @GetMapping(path="/searchProfile/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ModelAndView getSearchProfile(@PathVariable String id,
                                        ModelAndView mav){
        try{
            String[] userData = playerInfo.getTwitterMessage(id);
            mav.addObject("twitterMessage", userData[0]);
            mav.addObject("isDuplicated", userData[1]);
            mav.addObject("playerID",id);
            mav.setViewName("playerInfo");
            return mav;
        }catch (Exception e){
            mav.setViewName("redirect:/error");
            return mav;
        }
    }


    @PostMapping("/searchProfile")
    public String postSearchProfile(@RequestParam String code,
                                    @RequestParam String message,
                                    @RequestParam int bg) {
        PlayerDto playerDto = null;
        try {
            playerDto = playerInfo.resourceTest(code);
            playerInfo.makeProfileImg(playerDto, message, bg);
        } catch (DuplicatedUserException e) {
            System.out.println("user duplicated!");
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
        return "redirect:/searchProfile/" + code;
    }

}
