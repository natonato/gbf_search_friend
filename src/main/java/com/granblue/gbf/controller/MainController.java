package com.granblue.gbf.controller;

import com.granblue.gbf.dto.PlayerDto;
import com.granblue.gbf.service.PlayerInfo;
import com.granblue.gbf.service.TwitterUploadImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    private final PlayerInfo playerInfo;

    private final TwitterUploadImpl twitterUpload;

    @Autowired
    MainController(PlayerInfo playerInfo, TwitterUploadImpl twitterUpload){
        this.playerInfo = playerInfo;
        this.twitterUpload = twitterUpload;
    }

    @GetMapping("/")
    public String basicGetProfile(){
        return "index";
    }

    @PostMapping("/searchProfile")
    public String postSearchProfile(@RequestParam String code, @RequestParam String message,
            Model model){
        PlayerDto playerDto=null;
        try{
            playerDto = playerInfo.resourceTest(code, message, 0);
        }catch (Exception e){
            System.out.println("Error : searchProfile");
            e.printStackTrace();
        }

        model.addAttribute("playerInfo", playerDto);
        return "playerInfo";
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
        return "index";
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
