package com.granblue.gbf.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import com.granblue.gbf.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.granblue.gbf.service.TwitterUpload;

@Service
public class GBFResourceImpl implements GBFResource {
	//singleton
	private String[][] imgName= {
			{"miscFst","miscSnd"},
			{"fireFst","fireSnd"},{"waterFst","waterSnd"},
			{"earthFst","earthSnd"},{"windFst","windSnd"},
			{"lightFst","lightSnd"},{"darkFst","darkSnd"}	
	};

	TwitterUpload twitterUpload;

	@Autowired
	public GBFResourceImpl(TwitterUpload twitterUpload) {
		this.twitterUpload=twitterUpload;
		// TODO Auto-generated constructor stub
	}

	
	//get image from url
	@Override
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

					ImageIO.write(image, "jpg", file);
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void makeProfileImg(PlayerDto playerDto, String message, int imgType) throws IOException {
		//img option
		String[] imgName = new String[]{"pc_basic.jpg","mobile_basic.jpg"};

		//icon name
		String[] iconName = new String[]{"fire","water","earth","wind","light","dark","normal"};

		//summon start / summon distance / character / frame position
		int sx=0,sy=0,dx=0,dy=0, charx=0,chary=0, framex=0,framey=0,playerx=0,playery=0;
		if(imgType==0){
			//pc, 1920*1080 size
			sx=100; sy=600;
			dx=250; dy=200;
			charx=100; chary=0;
			framex=25; framey=450;
			playerx=1200; playery=190;
		}
		else if(imgType==1){
			//add later
		}

		//get bg img
		BufferedImage baseImgPng = ImageIO.read(new File("src/main/resources/static/img/bgimg/"+imgName[imgType]));
		BufferedImage baseImg = new BufferedImage(baseImgPng.getWidth(), baseImgPng.getHeight(), BufferedImage.TYPE_INT_RGB);
		baseImg.createGraphics().drawImage(baseImgPng, 0, 0, Color.white, null);

		//set font
		Graphics2D graphics = (Graphics2D)baseImg.getGraphics();


		//Get favorate character img
		StringTokenizer st = new StringTokenizer(playerDto.getFavorite(), "/");
		String favorate="";
		while(st.hasMoreTokens()){
			favorate = st.nextToken();
		}
		if("empty".equals(favorate.substring(0,favorate.length()-4))){
			//default is Lyria
			favorate = "http://game-a.granbluefantasy.jp/assets_en/img/sp/assets/npc/zoom/3030182000_01_03.png";
		}
		else {
			favorate = "http://game-a.granbluefantasy.jp/assets_en/img/sp/assets/npc/zoom/" + favorate.substring(0, favorate.length() - 3) + "png";
		}
		System.out.println(favorate);
		BufferedImage favorateImg = ImageIO.read(new URL(favorate));
		graphics.drawImage(favorateImg, charx,chary, null);

		//Draw summon background
		BufferedImage frameImg = ImageIO.read(new File("src/main/resources/static/img/icon/frame.png"));
		graphics.drawImage(frameImg,framex,framey, null);

		graphics.setColor(new Color(0,0,0));
		graphics.setFont(new Font("Open Sans", Font.BOLD, 30));
		graphics.drawString(playerDto.getName(), playerx, playery-10);

		graphics.drawString("Rank "+playerDto.getRank(), playerx+250, playery-10);

		System.out.println(message);
		for(String text : message.split("\n")){
			graphics.drawString(text, playerx-50, playery+=50);
		}

		graphics.setColor(new Color(255,255,255));
		graphics.setFont(new Font("Open Sans", Font.PLAIN, 20));
		BufferedImage image = null;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 2; j++) {
				try {
					int tempI = (i+6)%7;

					//draw icon
					image = ImageIO.read(new File("src/main/resources/static/img/icon/"+iconName[i]+".png"));
					Image resizedImage = image.getScaledInstance(image.getWidth()*2, image.getHeight()*2,Image.SCALE_SMOOTH);
					graphics.drawImage(resizedImage, sx+dx*i+70, sy-dy+100, null);

					//draw summon
					image = ImageIO.read(new URL(playerDto.getSummon()[i][j]));
					graphics.drawImage(image, sx+dx*tempI, sy+dy*j, null);

					//draw summmon lvl & name
					String summonName = playerDto.getSummonName()[i][j];
					if(summonName==null)summonName = "    No Summon";
					else summonName = "Lvl " + playerDto.getSummonLevel()[i][j] +" "+playerDto.getSummonName()[i][j];

					graphics.drawString(summonName, sx+dx*tempI+20, sy+dy*j+160);

				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		//save
		File file = new File("res/result/"+playerDto.getId()+"/merged_"+imgName[imgType]);
		if(!file.exists())file.mkdirs();
		ImageIO.write(baseImg, "jpg", file);

		twitterUpload.sendPlayerTweet(playerDto, message, file);
	}
	
}
