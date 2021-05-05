package userinfo;

public class UserInfo_temp {
	private String twitterID="YourTwitterID";;
	private String twitterPW="YourTwitterPW";;
	
	
	
	public String getTwitterID() {
		return twitterID;
	}

	public String getTwitterPW() {
		return twitterPW;
	}

	private static UserInfo_temp instance;
	private UserInfo_temp() {}
	
	public static UserInfo_temp getInstance() {
		if(instance==null)instance=new UserInfo_temp();
		return instance;
	}
}
