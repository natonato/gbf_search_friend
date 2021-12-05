package com.gbf.gbf_ff_1030.service;

import com.gbf.gbf_ff_1030.config.TwitterInfo;
import com.gbf.gbf_ff_1030.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.File;


@Service
@ComponentScan
public class TwitterUploadImpl implements TwitterUpload{

	private TwitterInfo twitterInfo;

	private Twitter twitter;
	private RequestToken requestToken=null;
	private AccessToken finalAccessToken=null;

	private static TwitterUploadImpl instance;

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

	@Override
	public void tweetTokenTest() {
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

	@Override
	public void tweetGetAccessTokenTest(String pin) {
        try {
			finalAccessToken = twitter.getOAuthAccessToken(requestToken, pin);
		}catch(TwitterException e) {
			e.printStackTrace();
		}
	}

	@Override
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

	@Override
	public void sendPlayerTweet(PlayerDto playerDto, String message) {
		String[] summonElement = new String[]{"Free","Fire","Water","Earth","Wind","Light","Dark"};
		twitter = TwitterFactory.getSingleton();
		try {
			File image = new File("res/result/"+playerDto.getId()+"/merged.jpg");
			User user = twitter.verifyCredentials();

			StringBuffer msg = new StringBuffer("ID:"+playerDto.getId()+"\n"
					+ "Name:"+playerDto.getName() +"\n");

			for(int i=0;i<7; i++){
				int idx = (i+1)%7;
				String sumName = playerDto.getSummonName()[idx][0];
				msg.append(summonElement[idx]).append(":");
				if(sumName!=null){
					msg.append(playerDto.getSummonLevel()[idx][0])
							.append(" ")
							.append(sumName)
							.append("/");
				}
				else{
					msg.append("No Summon/");
				}
				sumName = playerDto.getSummonName()[idx][1];
				if(sumName!=null){
					msg.append(playerDto.getSummonLevel()[idx][1])
							.append(" ")
							.append(sumName)
							.append("\n");
				}
				else {
					msg.append("No Summon\n");
				}
			}
			System.out.println(msg.length());

			StatusUpdate statusUpdate = new StatusUpdate(msg.toString());
			statusUpdate.setMedia(image);

			Status status = twitter.updateStatus(statusUpdate);

		}catch(Exception e) {
			e.getCause();
			e.printStackTrace();
		}


	}

}