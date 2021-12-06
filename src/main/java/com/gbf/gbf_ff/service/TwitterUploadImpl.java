package com.gbf.gbf_ff.service;

import com.gbf.gbf_ff.config.TwitterInfo;
import com.gbf.gbf_ff.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.File;


@Service
//@ComponentScan
public class TwitterUploadImpl implements TwitterUpload{


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

		twitter = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer(twitterInfo.getAPIKey(),
				twitterInfo.getAPISecretKey());

		finalAccessToken=new AccessToken(twitterInfo.getAccessToken(),
				twitterInfo.getAccessSecret());

		twitter.setOAuthAccessToken(finalAccessToken);
		
	}

	@Override
	public void sendPlayerTweet(String id) throws Exception {
		String[] summonElement = new String[]{"Free","Fire","Water","Earth","Wind","Light","Dark"};
		twitter = TwitterFactory.getSingleton();
		try {
			PlayerDto playerDto = playerInfo.resourceTest(id);

			File image = new File(imgSaveUrl+playerDto.getId()+"/merged.jpg");
			User user = twitter.verifyCredentials();

			StringBuffer msg = new StringBuffer("ID:"+playerDto.getId()+"\n"
					+ "Name:"+playerDto.getName() +"\n");

			for(int i=0;i<7; i++){
				int idx = (i+1)%7;
				String sumName = playerDto.getSummonName()[idx][0];
				msg.append(summonElement[idx]).append(":");
				if(sumName!=null){
					msg.append("Lv")
							.append(playerDto.getSummonLevel()[idx][0])
							.append(" ")
							.append(sumName)
							.append("/");
				}
				else{
					msg.append("No Summon/");
				}
				sumName = playerDto.getSummonName()[idx][1];
				if(sumName!=null){
					msg.append("Lv")
							.append(playerDto.getSummonLevel()[idx][1])
							.append(" ")
							.append(sumName)
							.append("\n");
				}
				else {
					msg.append("No Summon\n");
				}
			}

			if(msg.length()>278)msg.setLength(278);

			StatusUpdate statusUpdate = new StatusUpdate(msg.toString());
			statusUpdate.setMedia(image);

			Status status = twitter.updateStatus(statusUpdate);

		}catch(Exception e) {
			e.getCause();
			e.printStackTrace();
		}


	}

}
