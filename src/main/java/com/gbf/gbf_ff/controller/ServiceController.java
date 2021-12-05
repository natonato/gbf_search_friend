package com.gbf.gbf_ff.controller;

import com.gbf.gbf_ff.service.GBFResource;
import com.gbf.gbf_ff.service.PlayerInfo;
import com.gbf.gbf_ff.service.TwitterUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

    private final PlayerInfo playerInfo;

    private TwitterUpload twitterUpload;

    private GBFResource gbfResource;

    @Autowired
    ServiceController(PlayerInfo playerInfo, TwitterUpload twitterUpload,
                   GBFResource gbfResource){
        this.playerInfo = playerInfo;
        this.twitterUpload=twitterUpload;
        this.gbfResource=gbfResource;
    }

//    @PostMapping("/twitterCookieTest")
//    public void postTwitterCookieTest(){
//        try{
//            playerInfo.twitterCookieTest();
//        }catch (Exception e){
//            System.out.println("Error : twitterCookieTest");
//            e.printStackTrace();
//        }
//        return;// "redirect:/";
//    }

    @PostMapping("/sendTweet")
    public ResponseEntity postSendTweet(@RequestParam String id){
        try{
            twitterUpload.sendPlayerTweet(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);//"redirect:/searchProfile/"+id;
    }
}
