package service;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import userinfo.TwitterInfo;

public class TwitterUploadImpl {
	private static TwitterUploadImpl instance;
	private TwitterUploadImpl() {
		// TODO Auto-generated constructor stub
	}
	public static TwitterUploadImpl getInstance() {
		if(instance==null)instance=new TwitterUploadImpl();
		return instance;
	}
	
	public void tweetTokenTest() throws TwitterException {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(TwitterInfo.getInstance().getAPIKey(), 
				TwitterInfo.getInstance().getAPISecretKey());
		
		RequestToken requestToken = twitter.getOAuthRequestToken();
		AccessToken accessToken = null;
		
		//selenium으로 트위터 토큰 얻기
		
	}
	
	
	
}
