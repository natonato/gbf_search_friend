package com.gbf.gbf_ff_1030.controller;

import com.gbf.gbf_ff_1030.dto.PlayerDto;
import com.gbf.gbf_ff_1030.service.PlayerInfo;
import com.gbf.gbf_ff_1030.service.TwitterUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;

@Controller
public class MainController {

    private final PlayerInfo playerInfo;

    private TwitterUpload twitterUpload;

    @Autowired
    MainController(PlayerInfo playerInfo, TwitterUpload twitterUpload){
        this.playerInfo = playerInfo;
        this.twitterUpload=twitterUpload;
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

//    @GetMapping("/profileImg/{id}")
//    public String getProfileImg(@PathVariable String id,
//                                HttpServletResponse response){
//        response.setContentType("image/jpeg"); // Or whatever format you wanna use
//
//        return "error";
//    }




    @PostMapping("/searchProfile")
    public String postSearchProfile(@RequestParam String code,
                                    @RequestParam String message,
                                    @RequestParam int bg,
                                    ModelAndView mav) {
        PlayerDto playerDto=null;
        try{
            playerDto = playerInfo.resourceTest(code, message, bg);
        }catch (Exception e){
            System.out.println("Error : searchProfile");
            e.printStackTrace();
        }
//        mav.addObject("playerInfo", playerDto);
//        mav.setViewName("playerInfo");
        return "redirect:/searchProfile/"+playerDto.getId();
    }



    @PostMapping("/twitterTest")
    public String postTwitterTest(){
        try{
            playerInfo.twitterTest();
        }catch (Exception e){
            System.out.println("Error : twitterTest");
            e.printStackTrace();
        }
        return "index";
    }

    @PostMapping("/twitterCookieTest")
    public String postTwitterCookieTest(){
        try{
            playerInfo.twitterCookieTest();
        }catch (Exception e){
            System.out.println("Error : twitterCookieTest");
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @PostMapping("/tweetTest")
    public String postTweetTest(){
        try{
            twitterUpload.tweetTokenTest();
        }catch (Exception e){
            System.out.println("Error : twitterTest");
            e.printStackTrace();
        }
        return "index";
    }

    @PostMapping("/token")
    public String postTokenTest(@RequestParam String token){
        twitterUpload.tweetGetAccessTokenTest(token);
        return "index";
    }

    @PostMapping("/sendTweet")
    public String postSendTweetTest(){
        twitterUpload.sendTweetTest();
        return "index";
    }
}
