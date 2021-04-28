package userinfo;

public class UserInfo {
	private String twitterID="twitterID";;
	private String twitterPW="twitterPW";;
	
	
	
	public String getTwitterID() {
		return twitterID;
	}

	public String getTwitterPW() {
		return twitterPW;
	}

	private static UserInfo instance;
	private UserInfo() {}
	
	public static UserInfo getInstance() {
		if(instance==null)instance=new UserInfo();
		return instance;
	}
}
