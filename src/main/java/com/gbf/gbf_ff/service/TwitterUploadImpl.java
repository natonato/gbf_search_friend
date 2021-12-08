package com.gbf.gbf_ff.service;

import com.gbf.gbf_ff.Exception.DuplicatedUserException;
import com.gbf.gbf_ff.config.TwitterInfo;
import com.gbf.gbf_ff.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;


@Service
//@ComponentScan
public class TwitterUploadImpl implements TwitterUpload{

	private HashMap<String, String> saveDate;

	@Value("${image.location}")
	private String imgSaveUrl;

	private TwitterInfo twitterInfo;
	private PlayerInfo playerInfo;

	private Twitter twitter;
	private RequestToken requestToken=null;
	private AccessToken finalAccessToken=null;

	@Autowired
	public TwitterUploadImpl(TwitterInfo twitterInfo, PlayerInfo playerInfo) {
		this.playerInfo=playerInfo;
		this.twitterInfo = twitterInfo;

		saveDate = new HashMap<>();

		twitter = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer(twitterInfo.getAPIKey(),
				twitterInfo.getAPISecretKey());

		finalAccessToken=new AccessToken(twitterInfo.getAccessToken(),
				twitterInfo.getAccessSecret());

		twitter.setOAuthAccessToken(finalAccessToken);
		
	}

	@Override
	public void sendPlayerTweet(String id, String msg) throws Exception {

		//remove duplicated message / once a day
		String today = LocalDate.now().toString();
		if(saveDate.containsKey(id) && today.equals(saveDate.get(id)))throw new DuplicatedUserException();
		saveDate.put(id, today);

		twitter = TwitterFactory.getSingleton();
		try {

			File image = new File(imgSaveUrl+id+"/merged.jpg");
			User user = twitter.verifyCredentials();

			StatusUpdate statusUpdate = new StatusUpdate(msg);
			statusUpdate.setMedia(image);

			Status status = twitter.updateStatus(statusUpdate);

		}catch(Exception e) {
			e.getCause();
			e.printStackTrace();
		}


	}

}
