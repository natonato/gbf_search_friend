package com.gbf.gbf_ff.controller;

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

    private TwitterUpload twitterUpload;

    private GBFResource gbfResource;

    @Autowired
    MainController(PlayerInfo playerInfo, TwitterUpload twitterUpload,
                   GBFResource gbfResource){
        this.playerInfo = playerInfo;
        this.twitterUpload=twitterUpload;
        this.gbfResource=gbfResource;
    }

    @GetMapping("/")
    public String getHome(){
        return "index";
    }

    @GetMapping("/error")
    public String getError(){
        return "error";
    }

    @GetMapping(path="/searchProfile/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ModelAndView getSearchProfile(@PathVariable String id,
                                        ModelAndView mav){
        try (InputStream inputStream = new FileInputStream("src/main/resources/static/result/"+id+"/merged.jpg")){
            byte[] byteArray = IOUtils.toByteArray(inputStream);

            mav.addObject("playerID",id);
            mav.addObject("playerImg",Base64.getEncoder().encodeToString(byteArray));
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
                                    @RequestParam int bg,
                                    ModelAndView mav) {
        PlayerDto playerDto=null;
        try{
            playerDto = playerInfo.resourceTest(code);
            gbfResource.makeProfileImg(playerDto,message,bg);
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
        return "redirect:/searchProfile/"+playerDto.getId();
    }

}
