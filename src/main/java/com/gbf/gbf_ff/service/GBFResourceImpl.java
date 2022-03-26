package com.gbf.gbf_ff.service;

import com.gbf.gbf_ff.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.StringTokenizer;

@Service
//@ComponentScan
public class GBFResourceImpl implements GBFResource {

	@Value("${image.location}")
	private String imgSaveUrl;

	private String[][] imgName= {
			{"miscFst","miscSnd"},
			{"fireFst","fireSnd"},{"waterFst","waterSnd"},
			{"earthFst","earthSnd"},{"windFst","windSnd"},
			{"lightFst","lightSnd"},{"darkFst","darkSnd"}	
	};

	public GBFResourceImpl() {
		// TODO Auto-generated constructor stub
	}




	@Override
	public String makeProfileImg(PlayerDto playerDto, String message, int imgType) throws Exception {
		//img option
		String[] imgName = new String[]{"pc_1.jpg", "pc_2.jpg","pc_3.jpg", "pc_4.jpg"};
		String[] mobileName = new String[]{"mobile_1.jpg","mobile_2.jpg","mobile_3.jpg","mobile_4.jpg"};

		String curBgImg = imgName[0]; //default
		if(imgType<100)curBgImg=imgName[imgType];
		else curBgImg=mobileName[imgType-100];//

		//icon name
		String[] iconName = new String[]{"fire","water","earth","wind","light","dark","normal"};

		//summon start / summon distance / character / frame position
		int sx=0,sy=0,dx=0,dy=0, elementx=0, elementy=0,
				charx=0,chary=0, framex=0,framey=0,playerx=0,playery=0,profilex=0,profiley=0,
				sumnamex=0,sumnamey=0, iconx=0,icony=0;
		if(imgType<100){
			//pc, 1920*1080 size
			sx=100; sy=600; //start point of summon drawing
			dx=250; dy=0; // distance between summon in each element
			elementx=0; elementy=200; // distance between each element
			charx=100; chary=0; // where to draw favorite char
			framex=25; framey=450; // where to draw summon frame
			profilex=1060; profiley=100; // where to draw profile info
			playerx=profilex+140; playery=profiley+90; // for profile text
			sumnamex=20;sumnamey=160; // relative position for summon name based on summon image
			iconx=70;icony=100; // relative position for summon element icon(fire, water, etc) based on summon image
		}
		else{
			//mobile, 1080*1920 size
			sx=350; sy=770;
			dx=0; dy=150;
			elementx=300; elementy=0;
			charx=-100; chary=0;

			framex=100;framey=690;

			profilex=430; profiley=350;
			playerx=profilex+140; playery=profiley+90;
			sumnamex=20;sumnamey=140;

			iconx=150; icony=30;
		}

		//Get bg img
		BufferedImage bufferedImage = ImageIO.read(new File("src/main/resources/static/img/bgimg/"+curBgImg));
		BufferedImage baseImg = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		baseImg.createGraphics().drawImage(bufferedImage, 0, 0, Color.white, null);

		Graphics2D graphics = (Graphics2D)baseImg.getGraphics();

		//Draw favorate character img
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
			favorate = "http://game-a.granbluefantasy.jp/assets_en/img/sp/assets/npc/zoom/" + favorate.substring(0, favorate.length() - 4) + ".png";
		}
		bufferedImage = ImageIO.read(new URL(favorate));
		graphics.drawImage(bufferedImage, charx,chary, null);

		//Draw summon frame
		if(imgType<100)bufferedImage = ImageIO.read(new File("src/main/resources/static/img/icon/frame.png"));
		else bufferedImage = ImageIO.read(new File("src/main/resources/static/img/icon/frame_mobile.png"));
		graphics.drawImage(bufferedImage,framex,framey, null);

		//Draw profile img
		bufferedImage = ImageIO.read(new File("src/main/resources/static/img/icon/profile.png"));
		graphics.drawImage(bufferedImage, profilex, profiley, null);

		//Set font, draw ID
		graphics.setColor(new Color(255,255,255));
		graphics.setFont(new Font("Open Sans", Font.BOLD, 30));
		graphics.drawString("ID "+playerDto.getId(), playerx+100, playery-55);

		//Draw Name, Rank, Message
		graphics.setColor(new Color(0,0,0));
		graphics.drawString(playerDto.getName(), playerx, playery-10);
		graphics.drawString("Rank "+playerDto.getRank(), playerx+250, playery-10);
		int textCnt=0;
		for(String text : message.split("\n")){
			graphics.drawString(text, playerx-50, playery+=50);
			if(textCnt++ == 3)break;
		}

		//Set font
		graphics.setColor(new Color(255,255,255));
		graphics.setFont(new Font("Open Sans", Font.PLAIN, 20));

		//Draw Summon
//		BufferedImage bufferedImage = null;
		for (int i = 0; i < 7; i++) {
			//draw icon
			bufferedImage = ImageIO.read(new File("src/main/resources/static/img/icon/"+iconName[i]+".png"));
			Image resizedImage = bufferedImage.getScaledInstance(bufferedImage.getWidth()*2, bufferedImage.getHeight()*2,Image.SCALE_SMOOTH);
			graphics.drawImage(resizedImage, sx+dx*i-elementx+iconx, sy+dy*i-elementy+icony, null);

			for (int j = 0; j < 2; j++) {
				try {
					int tempI = (i+6)%7;

					//draw summon
					bufferedImage = ImageIO.read(new URL(playerDto.getSummon()[i][j]));
					graphics.drawImage(bufferedImage, sx+dx*tempI+elementx*j, sy+dy*tempI+elementy*j, null);

					//draw summmon lvl & name
					String summonName = playerDto.getSummonName()[i][j];
					if(summonName==null)summonName = "    No Summon";
					else summonName = "Lvl " + playerDto.getSummonLevel()[i][j] +" "+playerDto.getSummonName()[i][j];

					graphics.drawString(summonName, sx+dx*tempI+elementx*j+sumnamex, sy+dy*tempI+elementy*j+sumnamey);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

		//Save file
//		File file = new File(imgSaveUrl+playerDto.getId()+"/merged.jpg");
//		if(!file.exists())file.mkdirs();

//		System.out.println(file.getPath());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(baseImg, "jpg", baos);

		String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());

		System.out.println(base64Image);
		return base64Image;

//		ImageIO.write(baseImg, "jpg", file);

	}
	
}
