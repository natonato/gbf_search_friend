package com.granblue.gbf.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import com.granblue.gbf.config.TwitterInfo;
import com.granblue.gbf.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

@Service
@ComponentScan
public class TwitterUploadImpl {

	private final TwitterInfo twitterInfo;

	private Twitter twitter;
	private RequestToken requestToken=null;
	private AccessToken finalAccessToken=null;

	@Autowired
	public TwitterUploadImpl(TwitterInfo twitterInfo) {
		this.twitterInfo = twitterInfo;

		twitter = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer(twitterInfo.getAPIKey(),
				twitterInfo.getAPISecretKey());

		finalAccessToken=new AccessToken(twitterInfo.getAccessToken(),
				twitterInfo.getAccessSecret());
		
		twitter.setOAuthAccessToken(finalAccessToken);
		
	}

	
	public void tweetTokenTest() throws IOException {
		twitter = TwitterFactory.getSingleton();
		
		requestToken=null;
		try {
			requestToken = twitter.getOAuthRequestToken();
		}catch(TwitterException e) {
			e.printStackTrace();
		}
		
        System. out.println("1. TwitterClient.getRequestToken: " );
        System. out.println("1.1 Token: " +requestToken.getToken() + " ");
        System. out.println("1.2 TokenSecret: "+requestToken.getTokenSecret() + " ");
        System. out.println("1.3 getAuthorizationURL: " + requestToken.getAuthorizationURL());
        
        
	}
	
	public void tweetGetAccessTokenTest(String pin) {
        try {
			finalAccessToken = twitter.getOAuthAccessToken(requestToken, pin);
		}catch(TwitterException e) {
			e.printStackTrace();
		}
	}

	public void sendTweetTest() {
		twitter = TwitterFactory.getSingleton();
		try {
			User user = twitter.verifyCredentials();
			System.out.println(user.getScreenName());
			double rand = Math.random()*1000;
			String msg = "this is test tweet from application"+rand;
			Status status = twitter.updateStatus(msg);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public void sendPlayerTweet(PlayerDto playerDto, String message, File image) {
		twitter = TwitterFactory.getSingleton();
		try {
			User user = twitter.verifyCredentials();
			System.out.println(user.getScreenName());

			String msg = "ID : "+playerDto.getId() +"\n"
					+ Math.random();
//					+ "Name : "+playerDto.getName() +"\n"
//					+ "Rank : "+playerDto.getRank() +"\n"
//					+ "FireSummon : "+ playerDto.getSummonLevel()[0][0] + " " +playerDto.getSummonName()[0][0];

			long[] mediaIds = new long[1];
//			UploadedMedia media = twitter.uploadMedia(image);
//			mediaIds[0] = media.getMediaId();

			StatusUpdate statusUpdate = new StatusUpdate(msg);
//			statusUpdate.setMedia(image);
//			statusUpdate.setMediaIds(mediaIds);
			Status status = twitter.updateStatus(statusUpdate);

		}catch(Exception e) {

			e.printStackTrace();
		}


	}
}
