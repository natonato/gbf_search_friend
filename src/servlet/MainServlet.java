package servlet;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.PlayerDto;
import service.PlayerInfoImpl;
import service.TwitterUploadImpl;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/main")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doService(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}


	private void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String act = request.getParameter("act");
		System.out.println(act);
		switch (act) {
		case "searchProfile":
			searchProfile(request,response);
			break;
		case "twitterTest":
			twitterTest(request,response);
			break;
		case "twitterCookieTest":
			twitterCookieTest(request,response);
			break;
		case "tweetTest":
			tweetTest(request,response);
			break;
		case "token":
			token(request,response);
			break;
		case "tweetUserTest":
			tweetUserTest(request,response);
			break;
		default:
			break;
		}
		
	}

	private void tweetUserTest(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		TwitterUploadImpl.getInstance().tweetTest();
		
	}

	private void token(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		TwitterUploadImpl.getInstance().tweetGetAccessTokenTest(request.getParameter("token"));
	}

	private void tweetTest(HttpServletRequest request, HttpServletResponse response){
		// TODO Auto-generated method stub
		try {
			TwitterUploadImpl.getInstance().tweetTokenTest();
		}catch(Exception e) {
			System.out.println("????");
			e.printStackTrace();
		}
		
	}

	private void searchProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("code");
		PlayerDto playerDto=null;
		try {
			playerDto=PlayerInfoImpl.getInstance().resourceTest(id);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("playerInfo", playerDto);
		request.getRequestDispatcher("view/info.jsp").forward(request, response);
		
	}

	private void twitterCookieTest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			PlayerInfoImpl.getInstance().twitterCookieTest();
		}catch(Exception e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	private void twitterTest(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			PlayerInfoImpl.getInstance().twitterTest();;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}




	
	
}
