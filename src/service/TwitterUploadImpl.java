package service;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import userinfo.TwitterInfo;

public class TwitterUploadImpl {
	private static TwitterUploadImpl instance;
	private TwitterUploadImpl() {
		// TODO Auto-generated constructor stub
		twitter = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer(TwitterInfo.getInstance().getAPIKey(), 
				TwitterInfo.getInstance().getAPISecretKey());

		finalAccessToken=new AccessToken(TwitterInfo.getInstance().getAccessToken(), 
				TwitterInfo.getInstance().getAccessSecret());
		
		twitter.setOAuthAccessToken(finalAccessToken);
		
	}
	public static TwitterUploadImpl getInstance() {
		if(instance==null)instance=new TwitterUploadImpl();
		return instance;
	}
	
	static String accessToken="";
	static String accessSecret="";
	
	static Twitter twitter;
	RequestToken requestToken=null;
	AccessToken finalAccessToken=null;
	
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
	public void tweetTest() {
		twitter = TwitterFactory.getSingleton();
		try {
			User user = twitter.verifyCredentials();
			System.out.println(user.getScreenName());
			
			String msg = "this is test tweet from application";
			Status status = twitter.updateStatus(msg);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
