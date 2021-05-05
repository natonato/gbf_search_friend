package userinfo;

public class TwitterInfo_temp {
	private String APIKey="YourApiKey";
	private String APISecretKey="YourApiSecretKey";
	private String BearerToken="YourBearerToken";

	private static TwitterInfo_temp instance;
	private TwitterInfo_temp() {}
	
	public static TwitterInfo_temp getInstance() {
		if(instance==null)instance=new TwitterInfo_temp();
		return instance;
	}
	
	public String getAPIKey() {
		return APIKey;
	}
	public String getAPISecretKey() {
		return APISecretKey;
	}
	public String getBearerToken() {
		return BearerToken;
	}
	
	
	
}
