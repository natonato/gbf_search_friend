package service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import dto.PlayerDto;

public class GBFResoureceImpl {
	//singleton
	private String[][] imgName= {
			{"miscFst","miscSnd"},
			{"fireFst","fireSnd"},{"waterFst","waterSnd"},
			{"earthFst","earthSnd"},{"windFst","windSnd"},
			{"lightFst","lightSnd"},{"darkFst","darkSnd"}	
	};
	private static GBFResoureceImpl instance;
	private GBFResoureceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public static GBFResoureceImpl getInstance() {
		if(instance==null)instance = new GBFResoureceImpl();
		return instance;
	}
	
	//get image from url
	
	public void getImg(String[][] imgUrl, String id, Graphics2D graphics) {
		BufferedImage image = null;
		
		for (int i = 0; i < imgUrl.length; i++) {
			for (int j = 0; j < imgUrl[0].length; j++) {
				try {
					URL url = new URL(imgUrl[i][j]);
					
					image = ImageIO.read(url);
					
					File file = new File("res/img"+id+"/"+imgName[i][j]+".jpg");
					if(!file.exists()) {
						file.mkdirs();
					}
					System.out.println(file.getAbsolutePath());
					
					
					ImageIO.write(image, "jpg", file);
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void makeProfileImg(PlayerDto playerDto) throws IOException {
		
		System.out.println(playerDto.getBgimg());
		BufferedImage baseImgPng = ImageIO.read(new URL(playerDto.getBgimg()));
		BufferedImage baseImg = new BufferedImage(baseImgPng.getWidth(), baseImgPng.getHeight(), BufferedImage.TYPE_INT_RGB);
		baseImg.createGraphics().drawImage(baseImgPng, 0, 0, Color.white, null);
		
		int width = baseImg.getWidth();
		int height = baseImg.getHeight();
		
		Graphics2D graphics = (Graphics2D)baseImg.getGraphics();
		
//		getImg(playerDto.getSummon(), playerDto.getId(), graphics);
		
		BufferedImage image = null;
		Image resizedImage = null;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 2; j++) {
				try {
					image = ImageIO.read(new URL(playerDto.getSummon()[i][j]));
					resizedImage = image.getScaledInstance((int)(image.getWidth()*0.6), (int)(image.getHeight()*0.6), Image.SCALE_SMOOTH);
					
					graphics.drawImage(resizedImage, (width/5)*(j*2+1), (height/9)*(i+1), null);
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

		File file = new File("res/result/"+playerDto.getId()+"/merged.jpg");
		if(!file.exists())file.mkdirs();
		
		ImageIO.write(baseImg, "jpg", file);
		
	}
	
}
