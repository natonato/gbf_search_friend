package service;

public class TwitterUploadImpl {
	private static TwitterUploadImpl instance;
	private TwitterUploadImpl() {
		// TODO Auto-generated constructor stub
	}
	public static TwitterUploadImpl getInstance() {
		if(instance==null)instance=new TwitterUploadImpl();
		return instance;
	}
	
	
	
	
	
}
